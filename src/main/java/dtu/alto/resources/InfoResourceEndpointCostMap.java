package dtu.alto.resources;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import dtu.alto.base.ResponseEntityBase;
import dtu.alto.base.ResponseMeta;
import dtu.alto.cost.CostType;
import dtu.alto.endpoint.TypedEndpointAddr;
import dtu.alto.endpointcost.EndpointCostMapData;
import dtu.alto.endpointcost.EndpointDstCosts;
import dtu.alto.endpointcost.ReqEndpointCostMap;
import org.onlab.packet.IpAddress;
import org.onosproject.net.*;
import org.onosproject.net.host.HostService;
import org.onosproject.net.topology.TopologyService;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

/**
 * Created by s150924 on 4/23/17.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "meta",
        "endpoint-cost-map"
})

public class InfoResourceEndpointCostMap extends ResponseEntityBase {

    @JsonProperty("endpoint-cost-map")
    EndpointCostMapData endpointCostMap = null;

    @JsonIgnore
    int maxValue = Integer.MIN_VALUE;

    @JsonIgnore
    int minValue = Integer.MAX_VALUE;

    public void InfoResourceEndpointCostMap(){
        this.endpointCostMap = new EndpointCostMapData();
    }


    @JsonProperty("endpoint-cost-map")
    public EndpointCostMapData getEndpointCostMap() {
        return endpointCostMap;
    }

    @JsonProperty("endpoint-cost-map")
    public void setEndpointCostMap(EndpointCostMapData endpointCostMap) {
        this.endpointCostMap = endpointCostMap;
    }


    public void setCosts(ReqEndpointCostMap req, ResponseMeta rMeta,
                         HostService hostService, TopologyService topologyService){

        this.endpointCostMap = new EndpointCostMapData();

        int max, min;

        SortedMap<TypedEndpointAddr, EndpointDstCosts> costs = this.endpointCostMap.getEndPointCostMap();

        for(TypedEndpointAddr source : req.getEndpoints().getSrcs()){

            for(TypedEndpointAddr dest : req.getEndpoints().getDsts()){

                Integer cost = returnE2ECost(hostService, rMeta.getCostType(), source, dest, topologyService);

                maxValue = (cost > maxValue) ? cost : maxValue;
                minValue = (cost < minValue) ? cost : minValue;

                if(cost != -1) {

                    if (!costs.containsKey(source)) {
                        EndpointDstCosts destCosts = new EndpointDstCosts();
                        costs.put(source, destCosts);
                    }
                    costs.get(source).getDstCosts().put(dest, cost);
                }

            }

            for(TypedEndpointAddr dest : req.getEndpoints().getDsts()) {
                for (Map.Entry<TypedEndpointAddr, Double> wcost : costs.get(source).getNormalizedCosts().entrySet()) {

                    double weightedCost;

                    if(maxValue != minValue){
                        weightedCost = (costs.get(source).getDstCosts().get(dest).doubleValue() - minValue) /
                                        (maxValue - minValue);
                    }else{
                        weightedCost = 0.5;
                    }

                    costs.get(source).getNormalizedCosts().put(dest, weightedCost);

                }
            }

        }



        if(rMeta.getCostType().getCostMode().equals("ordinal"))
            this.endpointCostMap = EndpointCostMapData.createOrdinalMap(this.endpointCostMap);

    }

    public Integer returnE2ECost(HostService hostService, CostType ct,
            TypedEndpointAddr source, TypedEndpointAddr dest, TopologyService topologyService){

        IpAddress sourceIP = IpAddress.valueOf(source.getEndpointAddr().getAddress());
        IpAddress destIP = IpAddress.valueOf(dest.getEndpointAddr().getAddress());

        DeviceId sourceDev = hostService.getHostsByIp(sourceIP).iterator().next().location().deviceId();
        DeviceId destDev = hostService.getHostsByIp(destIP).iterator().next().location().deviceId();

        double cost = 0;

        Set<Path> paths = topologyService.getPaths(
                topologyService.currentTopology(),
                sourceDev,
                destDev
        );

        if (paths.size() > 0) {
            for (Path p : paths) {
                if (cost == 0 || p.cost() < cost) {
                    cost = p.cost();
                }
            }
        }

        int finalcost = (ct.getCostMetric().equals("hopcount")) ? (int)cost+1 : (int)cost+2;

        return finalcost;

        /*
        *                                               __        __
        *                                               |            |
        *                                               |    |--- h2 |
        *  PID1 -> [ h1 -- ] -- x ------ x ------- x -- | -- |--- h3 | <- PID2
        *                     r1       r2        r3     |    |--- h4 |
        *                                               |__        __|
        *
        *
        *
        * by def, path.cost() from r1 to r3 is 2,
        * then it counts as traversed links
        *
        * add 1 to cost to get the hopcount from
        * hosts of each pid.
        * subtract 1 to cost ot get the hopcount
        * taking the ref switches as units of traffic source
        *
        * */

        //if we reach here, there should be a cost to return
    }
}
