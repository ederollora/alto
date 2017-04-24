package dtu.alto.cost;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import dtu.alto.pid.PIDName;

import java.io.Serializable;
import java.util.*;

/**
 * Created by s150924 on 3/15/17.
 */

public class DstCosts implements Serializable {

    SortedMap<PIDName, Integer> dstCosts = new TreeMap<>();


    public DstCosts(){
        dstCosts = new TreeMap<>();
    }

    public DstCosts(PIDName name, Integer cost){
        this();
        this.insertCost(name, cost);
    }

    @JsonAnyGetter
    public SortedMap<PIDName, Integer> getDstCosts() {
        return dstCosts;
    }

    @JsonAnySetter
    public void setDstCosts(SortedMap<PIDName, Integer> dstCosts) {
        this.dstCosts = dstCosts;
    }

    public void insertCost(PIDName name, Integer cost){

        this.dstCosts.put(name, cost);

    }


}
