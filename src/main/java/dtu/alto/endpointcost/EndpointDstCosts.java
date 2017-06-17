package dtu.alto.endpointcost;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import dtu.alto.endpoint.TypedEndpointAddr;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by s150924 on 4/23/17.
 */


public class EndpointDstCosts {

    SortedMap<TypedEndpointAddr, Integer> dstCosts = null;

    @JsonIgnore
    SortedMap<TypedEndpointAddr, Double> normalized = null;

    public EndpointDstCosts(){

        this.dstCosts = new TreeMap<>();
        this.normalized = new TreeMap<>();
    }

    @JsonAnyGetter
    public SortedMap<TypedEndpointAddr, Integer> getDstCosts() {
        return dstCosts;
    }

    @JsonAnySetter
    public void setDstCosts(SortedMap<TypedEndpointAddr, Integer> dstCosts) {
        this.dstCosts = dstCosts;
    }

    @JsonIgnore
    public SortedMap<TypedEndpointAddr, Double> getNormalizedCosts() {
        return normalized;
    }

    @JsonIgnore
    public void setNormalizedCosts(SortedMap<TypedEndpointAddr, Double> normalized) {
        this.normalized = normalized;
    }
}

