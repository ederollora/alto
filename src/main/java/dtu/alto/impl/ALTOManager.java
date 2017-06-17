package dtu.alto.impl;

import dtu.alto.base.SupportedCostTypes;
import dtu.alto.base.VersionTag;
import dtu.alto.cdn.RankedValues;
import dtu.alto.cdn.ServerReport;
import dtu.alto.cdn.ServerStatistics;
import dtu.alto.core.LoadCheckService;
import dtu.alto.cost.CostMapData;
import dtu.alto.cost.CostType;
import dtu.alto.endpoint.TypedEndpointAddr;
import dtu.alto.endpointcost.EndpointDstCosts;
import dtu.alto.endpointcost.WeightedEndpointCostMapData;
import dtu.alto.endpointcost.WeightedEndpointDstCosts;
import dtu.alto.linkload.PortStats;
import dtu.alto.resources.InfoResourceCostMap;
import dtu.alto.resources.InfoResourceEndpointCostMap;
import dtu.alto.resources.InfoResourceNetworkMap;
import dtu.alto.pid.PIDName;
import dtu.alto.core.ALTOService;
import dtu.alto.resources.InfoResourceWeightedEndpointCostMap;
import org.apache.felix.scr.annotations.*;
import org.onlab.packet.IpAddress;
import org.onosproject.net.Host;

import org.onosproject.net.device.DeviceService;
import org.onosproject.net.host.HostAdminService;
import org.onosproject.net.host.HostEvent;
import org.onosproject.net.host.HostListener;
import org.onosproject.net.host.HostService;

import org.onosproject.net.topology.TopologyEvent;
import org.onosproject.net.topology.TopologyListener;
import org.onosproject.net.topology.TopologyService;
import org.slf4j.Logger;

import java.util.*;

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

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected HostAdminService hostAdminService;

    protected LoadCheckService loadCheckService;

    private HostListener hostListener = new InnerHostListener();
    private TopologyListener topologyListener = new InnerTopologyListener();

    private InfoResourceNetworkMap infoResNetworkMap;
    private InfoResourceCostMap infoResCostMap;

    private int geninforesmap = 0;
    private int gencostresmap = 0;

    private List<VersionTag> latestVtags = new ArrayList<>();

    private SupportedCostTypes costTypes;

    private ServerReport cdnServerStats = new ServerReport();


    @Activate
    public void activate() {

        costTypes = new SupportedCostTypes();

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
    public Map<PIDName,List<Host>> getPIDs(){
        return infoResNetworkMap.getPIDs();
    }

    @Override
    public List<VersionTag> getAllVersionTags() {
        return latestVtags;
    }

    @Override
    public void rebuildMaps() {
        buildMaps();
    }

    @Override
    public void updateServerReport(ServerReport sReport) {
        this.cdnServerStats = sReport;
    }

    @Override
    public ServerReport getServerReport() {
        return this.cdnServerStats;
    }

    @Override
    public void getRankedEndpoints(InfoResourceEndpointCostMap eCostMap) {

        //HashMap<TypedEndpointAddr, PortStats> loadReport = loadCheckService.getLoadReport();

        Map<TypedEndpointAddr, ServerStatistics> serverStats = getServerReport().getServerStats();

        SortedMap<TypedEndpointAddr, EndpointDstCosts> endPointCosts = eCostMap.getEndpointCostMap().getEndPointCostMap();

        InfoResourceWeightedEndpointCostMap weCostMap = new InfoResourceWeightedEndpointCostMap();
        WeightedEndpointCostMapData weCostMapData = weCostMap.getEndpointCostMap();
        SortedMap<TypedEndpointAddr, WeightedEndpointDstCosts> weDstCosts =  weCostMapData.getWeightedEndPointCostMap();


        double weightBw = 0.7, weightLoad = 0.4, weightHop = 0.3;

        for(TypedEndpointAddr sourceaddr : endPointCosts.keySet()){

            weDstCosts.put(sourceaddr, new WeightedEndpointDstCosts());

            for (Map.Entry<TypedEndpointAddr, ServerStatistics> server : serverStats.entrySet()){

                SortedMap<TypedEndpointAddr, Double> weightedDstCosts = weDstCosts.get(sourceaddr).getDstCosts();

                //PortStats serverPortStats = loadReport.get(server.getKey());

                log.info("IP: "+server.getKey().getEndpointAddr().getAddress()+". Available per client: "+String.valueOf(server.getValue().getPerClientRate()));

                /*double finalcost = (((double)1 - (server.getValue().getNormalizedCapacity())) * weightBw) +
                                   (endPointCosts.get(sourceaddr).getNormalizedCosts().get(server.getKey()) * weightHop);*/
                                   //(server.getValue().getLoad() * weightLoad);

                double finalcost = (((double)1 - (server.getValue().getNormalizedAvail())) * weightBw) +
                        (endPointCosts.get(sourceaddr).getNormalizedCosts().get(server.getKey()) * weightHop);

                log.info("Finalcost of "+server.getKey().getEndpointAddr().getAddress()+". Cost: "+String.valueOf(finalcost));

                weightedDstCosts.put(server.getKey(), finalcost);
            }
        }

        WeightedEndpointCostMapData.createOrdinalMap(weCostMapData, eCostMap.getEndpointCostMap());

    }

    @Override
    public List<TypedEndpointAddr> getContentServers() {

        List<TypedEndpointAddr> listIps = new ArrayList<>();

        log.info("Size of servers: "+cdnServerStats.getServerStats().size());

        for (ServerStatistics stats : cdnServerStats.getServerStats().values()){

            //IpAddress ip = new IpAddress(stats.getIpAddress());

            listIps.add(stats.getIpAddress());
        }

        return listIps;
    }

    private void buildNetworkMap(){

        geninforesmap++;

        log.info("Build Network Map number: "+String.valueOf(geninforesmap));

        Iterable<Host> hosts = hostService.getHosts();

        infoResNetworkMap = new InfoResourceNetworkMap(hosts, log, deviceService, hostAdminService);

        //addVtagToList(infoResNetworkMap.VersionTag());

    }

    private void buildCostMap(){

        gencostresmap++;

        log.info("Build Cost Map number: "+String.valueOf(gencostresmap));

        infoResCostMap = new InfoResourceCostMap(
                infoResNetworkMap.getMeta().getVersionTag()

        );

        for(CostType cType : costTypes.getSupportTypes()){
            if (cType.equals(SupportedCostTypes.NUM_ROUTING_COST)) {

                Map<CostType, CostMapData> costSet = infoResCostMap.getSetOfCostMaps();

                CostMapData nRoutingcostData = CostMapData.numRoutingCostMap(
                        infoResNetworkMap.getPIDs(), log, topologyService);

                costSet.put(
                    cType,
                    CostMapData.numRoutingCostMap(
                            infoResNetworkMap.getPIDs(),
                            log,
                            topologyService)
                );

                costSet.put(cType, nRoutingcostData);

                CostType ordCostType = new CostType("ordinal", cType.getCostMetric());

                costSet.put(ordCostType, CostMapData.createOrdinalMap(nRoutingcostData));

            }else if(cType.equals(SupportedCostTypes.NUM_HOPCOUNT)) {

                Map<CostType, CostMapData> costSet = infoResCostMap.getSetOfCostMaps();

                CostMapData nHopCountData = CostMapData.hopCountCostMap(
                        infoResNetworkMap.getPIDs(), log, topologyService);

                costSet.put(cType, nHopCountData);

                CostType ordCostType = new CostType("ordinal", cType.getCostMetric());

                costSet.put(ordCostType, CostMapData.createOrdinalMap(nHopCountData));
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

        for(int i = 1; i < latestVtags.size(); i++)
            latestVtags.add(i-1, latestVtags.get(i));

        latestVtags.remove(latestVtags.size()-1);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
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
