package me.ollora.thesis.alto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by s150924 on 3/15/17.
 */

public class DstCosts implements Serializable {

    private Map<String, Integer> dstCosts = null;

    public DstCosts(){
        dstCosts = new LinkedHashMap<>();
    }

    public DstCosts(String pidName, Integer cost){
        this();
        this.insertCost(pidName, cost);
    }

    @JsonAnyGetter
    public Map<String, Integer> getDstCosts() {
        return dstCosts;
    }

    @JsonAnySetter
    public void setDstCosts(Map<String, Integer> dstCosts) {
        this.dstCosts = dstCosts;
    }

    public void insertCost(String pidName, Integer cost){

        this.dstCosts.put(pidName, cost);

    }


}
