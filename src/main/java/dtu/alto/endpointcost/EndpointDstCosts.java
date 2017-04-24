package dtu.alto.endpointcost;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import dtu.alto.endpoint.TypedEndpointAddr;

import java.util.SortedMap;

/**
 * Created by s150924 on 4/23/17.
 */


public class EndpointDstCosts {

    SortedMap<TypedEndpointAddr, Integer> dstCosts = null;

    @JsonAnyGetter
    public SortedMap<TypedEndpointAddr, Integer> getDstCosts() {
        return dstCosts;
    }

    @JsonAnySetter
    public void setDstCosts(SortedMap<TypedEndpointAddr, Integer> dstCosts) {
        this.dstCosts = dstCosts;
    }


}

