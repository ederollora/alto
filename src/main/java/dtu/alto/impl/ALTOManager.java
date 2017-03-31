package dtu.alto.impl;

import dtu.alto.base.SupportedCostTypes;
import dtu.alto.base.VersionTag;
import dtu.alto.cost.CostMapData;
import dtu.alto.cost.CostType;
import dtu.alto.resources.InfoResourceCostMap;
import dtu.alto.resources.InfoResourceNetworkMap;
import dtu.alto.pid.PID;
import dtu.alto.core.ALTOService;
import org.apache.felix.scr.annotations.*;
import org.onosproject.net.Host;

import org.onosproject.net.device.DeviceService;
import org.onosproject.net.host.HostEvent;
import org.onosproject.net.host.HostListener;
import org.onosproject.net.host.HostService;

import org.onosproject.net.topology.TopologyEvent;
import org.onosproject.net.topology.TopologyListener;
import org.onosproject.net.topology.TopologyService;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by s150924 on 3/22/17.
 */


@Component(immediate = true)
@Service
public class ALTOManager implements ALTOService {

    private final Logger log = getLogger(getClass());

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected HostService hostService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected DeviceService deviceService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected TopologyService topologyService;

    private HostListener hostListener = new InnerHostListener();
    private TopologyListener topologyListener = new InnerTopologyListener();

    private InfoResourceNetworkMap infoResNetworkMap;
    private InfoResourceCostMap infoResCostMap;

    private int geninforesmap = 0;
    private int gencostresmap = 0;

    private List<VersionTag> latestVtags = new ArrayList<>();

    private List<CostType> costTypes = new ArrayList<>();


    @Activate
    public void activate() {

        costTypes.add(new CostType("numerical", "hopcount"));

        buildMaps(); //It also builds the cost map

        hostService.addListener(hostListener);
        topologyService.addListener(topologyListener);

        log.info("Started ALTO Service");
    }

    @Deactivate
    public void deactivate() {

        hostService.removeListener(hostListener);
        topologyService.removeListener(topologyListener);

        log.info("Stopped ALTO Service");
    }

    @Override
    public InfoResourceNetworkMap getNetworkMap() {
        return infoResNetworkMap;
    }

    @Override
    public InfoResourceCostMap getCostMap() {
        return infoResCostMap;
    }

    @Override
    public Map<PID,List<Host>> getPIDs(){
        return infoResNetworkMap.getPIDs();
    }

    @Override
    public List<VersionTag> getAllVersionTags() {
        return latestVtags;
    }

    private void buildNetworkMap(){

        geninforesmap++;

        log.info("Build Network Map number: "+String.valueOf(geninforesmap));

        Iterable<Host> hosts = hostService.getHosts();

        infoResNetworkMap = new InfoResourceNetworkMap(hosts, log, deviceService);

        addVtagToList(infoResNetworkMap.VersionTag());

    }

    private void buildCostMap(){

        gencostresmap++;

        log.info("Build Cost Map number: "+String.valueOf(gencostresmap));

        infoResCostMap = new InfoResourceCostMap(
                infoResNetworkMap.getMeta().getVersionTag()

        );

        for (CostType cType : costTypes) {
            if (cType.equals(SupportedCostTypes.NUM_HOP_COUNT)) {
                infoResCostMap.getSetOfCostMaps().put(
                        cType,
                        CostMapData.numHopCountCostMap(
                                infoResNetworkMap.getPIDs(),
                                log,
                                topologyService)
                );
            }
        }

    }

    private void buildMaps(){

        buildNetworkMap();
        buildCostMap();
    }

    private void addVtagToList(VersionTag tag){

        if(latestVtags.size() == 5){
            shiftVtags();
        }

        latestVtags.add(tag);

    }

    private void shiftVtags(){

        for(int i = 0; i < (latestVtags.size()-2); i++)
            latestVtags.add(i, latestVtags.get(i+1));

        latestVtags.remove(latestVtags.size()-1);
    }

    private class InnerHostListener implements HostListener {

        @Override
        public void event(HostEvent hostEvent) {

            switch (hostEvent.type()){
                case HOST_ADDED:
                case HOST_REMOVED:
                    log.info("Rebuilding network map. Reason: "+hostEvent.type().toString()+" -> "+hostEvent.subject().id());
                    buildMaps();
                    break;
                case HOST_UPDATED:
                    log.info("Rebuilding network map. Reason: HOST_UPDATED -> "+hostEvent.subject().id());
                    break;
                case HOST_MOVED:
                    log.info("Rebuilding network map. Reason: HOST_MOVED -> "+hostEvent.subject().id());
                    break;
                default:
                    break;
            }
        }
    }

    private class InnerTopologyListener implements TopologyListener{

        @Override
        public void event(TopologyEvent topologyEvent) {

            switch (topologyEvent.type()){

                case TOPOLOGY_CHANGED:
                    log.info("Rebuilding network map. Reason: "+topologyEvent.type().toString()+" -> "+topologyEvent.subject());
                    buildMaps();
                    break;
                default:
                    break;

            }
        }
    }

}
