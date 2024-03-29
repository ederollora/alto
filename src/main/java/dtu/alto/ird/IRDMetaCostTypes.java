package dtu.alto.ird;

import dtu.alto.cost.CostType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by s150924 on 3/13/17.
 */
public class IRDMetaCostTypes implements Serializable {

    private Map<String, CostType> costTypes;

    public IRDMetaCostTypes(){


    }

    public IRDMetaCostTypes(IRDMetaCostTypes irdMetaCostTypes){
        this.costTypes = new HashMap<String, CostType>(irdMetaCostTypes.getCostTypes());

    }



    public Map<String, CostType> getCostTypes() {
        return costTypes;
    }

    public void setCostTypes(Map<String, CostType> costTypes) {
        this.costTypes = costTypes;
    }

}
