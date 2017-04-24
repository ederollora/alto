package dtu.alto.resources;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import dtu.alto.base.ResponseEntityBase;
import dtu.alto.cost.DstCosts;
import dtu.alto.endpoint.TypedEndpointAddr;
import dtu.alto.endpointcost.EndpointCostMapData;
import dtu.alto.endpointcost.EndpointDstCosts;
import dtu.alto.endpointcost.ReqEndpointCostMap;
import dtu.alto.pid.PIDName;
import org.onosproject.net.Host;

import java.util.List;
import java.util.Map;
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


    public void setCosts(InfoResourceCostMap costMap,
                         Map<PIDName,List<Host>> pids,
                         ReqEndpointCostMap req){

        this.endpointCostMap = new EndpointCostMapData();

        SortedMap<TypedEndpointAddr, EndpointDstCosts> costs = this.endpointCostMap.geteCostMap();

        for(TypedEndpointAddr source : req.getEndpoints().getSrcs()){

            for(TypedEndpointAddr dest : req.getEndpoints().getDsts()){

                Integer cost = returnE2ECost(costMap, pids, source, dest);

                if(cost != -1) {

                    if (!costs.containsKey(source)) {
                        EndpointDstCosts destCosts = new EndpointDstCosts();
                        costs.put(source, destCosts);
                    }
                    costs.get(source).getDstCosts().put(dest, cost);
                }
            }
        }
    }

    public Integer returnE2ECost(InfoResourceCostMap costMap, Map<PIDName,List<Host>> pids,
            TypedEndpointAddr source, TypedEndpointAddr dest){

        PIDName sourcePID = null, destPID = null;
        SortedMap<PIDName, DstCosts> costs = costMap.getCostMap().getData();

        //search were the PID for source/destination endpoints
        for(Map.Entry<PIDName, List<Host>> pid : pids.entrySet()) {

            for (Host host : pid.getValue()) {
                if (source.equals(host.ipAddresses().iterator().next()))
                    sourcePID = pid.getKey();

                if (dest.equals(host.ipAddresses().iterator().next()))
                    destPID = pid.getKey();
            }

        }

        //Separated for readability

        //if source or dest PIDs were not found
        if(sourcePID == null || destPID == null)
            return new Integer(-1);

        //if source is not in the cost map
        if(!costs.containsKey(sourcePID))
            return new Integer(-1);

        //if no cost from source to destination
        if(!costs.get(sourcePID).getDstCosts().containsKey(destPID))
            return new Integer(-1);

        //if we reach here, there should be a cost to return
        return new Integer(costs.get(sourcePID).getDstCosts().get(destPID));

    }

}
