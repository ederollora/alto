package me.ollora.thesis.alto;

import org.apache.felix.scr.annotations.*;
import org.onosproject.net.Device;
import org.onosproject.net.Host;
import org.onosproject.net.device.DeviceEvent;
import org.onosproject.net.device.DeviceListener;
import org.onosproject.net.device.DeviceService;
import org.onosproject.net.host.HostEvent;
import org.onosproject.net.host.HostListener;
import org.onosproject.net.host.HostService;

import org.onosproject.net.link.LinkEvent;
import org.onosproject.net.link.LinkListener;
import org.onosproject.net.link.LinkService;
import org.onosproject.net.topology.TopologyEvent;
import org.onosproject.net.topology.TopologyListener;
import org.onosproject.net.topology.TopologyService;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by s150924 on 3/22/17.
 */


@Component(immediate = true)
@Service
public class ALTOManager implements ALTOService{

    private final Logger log = getLogger(getClass());


    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected HostService hostService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected DeviceService deviceService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected LinkService linkService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected TopologyService topologyService;

    private HostListener hostListener = new InnerHostListener();
    private DeviceListener deviceListener = new InnerDeviceListener();
    private LinkListener linkListener = new InnerLinkListener();
    private TopologyListener topologyListener = new InnerTopologyListener();

    private InfoResourceNetworkMap infoResNetworkMap;
    private InfoResourceCostMap infoResCostMap;

    private int geninforesmap = 0;
    private int gencostresmap = 0;


    @Activate
    public void activate() {

        buildNetworkMap(); //It also builds the cost map

        hostService.addListener(hostListener);
        //deviceService.addListener(deviceListener);
        //linkService.addListener(linkListener);
        topologyService.addListener(topologyListener);

        log.info("Started Alto Service");

    }

    @Deactivate
    public void deactivate() {

        hostService.removeListener(hostListener);
        //deviceService.removeListener(deviceListener);
        //linkService.removeListener(linkListener);
        topologyService.removeListener(topologyListener);

        log.info("Stopped Alto Service");
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

    private class InnerHostListener implements HostListener {

        @Override
        public void event(HostEvent hostEvent) {

            switch (hostEvent.type()){
                case HOST_ADDED:
                case HOST_REMOVED:
                    log.info("Rebuilding network map. Reason: "+hostEvent.type().toString()+" -> "+hostEvent.subject().id());
                    buildNetworkMap();
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
                    buildNetworkMap();
                    break;
                default:
                    break;

            }
        }
    }

    private class InnerDeviceListener implements DeviceListener {

        @Override
        public void event(DeviceEvent deviceEvent) {

            try {
                switch (deviceEvent.type()) {
                    case DEVICE_ADDED:
                    case DEVICE_REMOVED:
                        log.info("Rebuilding network map. Reason: "+deviceEvent.type().toString()+" -> "+deviceEvent.subject().id());
                        //buildNetworkMap();
                        break;
                    case DEVICE_UPDATED:
                        log.info("Rebuilding network map. Reason: DEVICE_UPDATED -> "+deviceEvent.subject().id());
                        break;
                    case DEVICE_SUSPENDED:
                        log.info("Rebuilding network map. Reason: DEVICE_SUSPENDED -> "+deviceEvent.subject().id());
                        break;
                    case DEVICE_AVAILABILITY_CHANGED:
                        log.info("Rebuilding network map. Reason: DEVICE_AVAILABILITY_CHANGED -> "+deviceEvent.subject().id());
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                log.warn("Failed to process {}", deviceEvent, e);
            }
        }
    }

    private class InnerLinkListener implements LinkListener{

        @Override
        public void event(LinkEvent linkEvent) {

            try {
                switch (linkEvent.type()){
                    case LINK_ADDED:
                    case LINK_REMOVED:
                        log.info("Rebuilding network map. Reason: "+
                                linkEvent.type().toString()+
                                " -> "+
                                linkEvent.subject().src()+" <-> "+linkEvent.subject().dst());
                        //buildNetworkMap();
                        break;
                    case LINK_UPDATED:
                        log.info("Rebuilding network map. Reason: "+
                                linkEvent.type().toString()+
                                " -> "+
                                linkEvent.subject().src()+" <-> "+linkEvent.subject().dst());
                    default:
                        break;
                }

            } catch (Exception e) {
                log.warn("Failed to process {}", linkEvent, e);
            }
        }
    }

    private void buildNetworkMap(){

        geninforesmap++;

        log.info("Build Network Map number: "+String.valueOf(geninforesmap));

        Iterable<Host> hosts = hostService.getHosts();

        infoResNetworkMap = new InfoResourceNetworkMap(hosts, log, deviceService);

        buildCostMap();

    }

    private void buildCostMap(){

        gencostresmap++;

        log.info("Build Cost Map number: "+String.valueOf(gencostresmap));

        infoResCostMap = new InfoResourceCostMap(
                infoResNetworkMap,
                log,
                topologyService
        );
    }

}
