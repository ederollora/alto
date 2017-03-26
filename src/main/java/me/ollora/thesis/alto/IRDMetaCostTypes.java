package me.ollora.thesis.alto;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by s150924 on 3/13/17.
 */
public class IRDMetaCostTypes implements Serializable {

    private Map<String, CostType> costTypes;

    public IRDMetaCostTypes(){


    }

    public Map<String, CostType> getCostTypes() {
        return costTypes;
    }

    public void setCostTypes(Map<String, CostType> costTypes) {
        this.costTypes = costTypes;
    }

}
