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


public class WeightedEndpointDstCosts {

    SortedMap<TypedEndpointAddr, Double> dstCosts = null;


    public WeightedEndpointDstCosts(){
        this.dstCosts = new TreeMap<>();
    }

    @JsonAnyGetter
    public SortedMap<TypedEndpointAddr, Double> getDstCosts() {
        return dstCosts;
    }

    @JsonAnySetter
    public void setDstCosts(SortedMap<TypedEndpointAddr, Double> dstCosts) {
        this.dstCosts = dstCosts;
    }


}

