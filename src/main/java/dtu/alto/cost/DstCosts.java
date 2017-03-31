package dtu.alto.cost;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.*;

/**
 * Created by s150924 on 3/15/17.
 */

public class DstCosts implements Serializable {


    SortedMap<String, Integer> dstCosts = new TreeMap<>();

    @JsonIgnore
    private Map<String, Integer> unorderedData = null;

    public DstCosts(){
        dstCosts = new TreeMap<>();
    }

    public DstCosts(String pidName, Integer cost){
        this();
        this.insertCost(pidName, cost);
    }

    @JsonAnyGetter
    public SortedMap<String, Integer> getDstCosts() {
        return dstCosts;
    }

    @JsonAnySetter
    public void setDstCosts(SortedMap<String, Integer> dstCosts) {
        this.dstCosts = dstCosts;
    }

    public void insertCost(String pidName, Integer cost){

        this.dstCosts.put(pidName, cost);

    }


}
