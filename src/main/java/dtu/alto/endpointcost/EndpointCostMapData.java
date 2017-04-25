package dtu.alto.endpointcost;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import dtu.alto.endpoint.TypedEndpointAddr;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by s150924 on 4/23/17.
 */
public class EndpointCostMapData {


    SortedMap<TypedEndpointAddr, EndpointDstCosts> eCostMap = null;

    public EndpointCostMapData(){
        this.eCostMap = new TreeMap<>();
    }

    @JsonAnyGetter
    public SortedMap<TypedEndpointAddr, EndpointDstCosts> geteCostMap() {
        return eCostMap;
    }

    @JsonAnySetter
    public void seteCostMap(SortedMap<TypedEndpointAddr, EndpointDstCosts> eCostMap) {
        this.eCostMap = eCostMap;
    }

}
