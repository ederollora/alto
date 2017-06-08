package dtu.alto.base;

import dtu.alto.cost.CostType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by s150924 on 3/30/17.
 */
public class SupportedCostTypes {

    public static final CostType NUM_ROUTING_COST = new CostType("numerical", "routingcost");

    public static final CostType ORDINAL_ROUTING_COST = new CostType("ordinal", "routingcost");

    List<CostType> supportTypes;

    public SupportedCostTypes() {
        supportTypes = new ArrayList<>();
        supportTypes.add(new CostType("numerical", "routingcost"));
        //supportTypes.add(new CostType("ordinal", "routingcost"));
    }

    public List<CostType> getSupportTypes() {
        return supportTypes;
    }

    public void setSupportTypes(List<CostType> supportTypes) {
        this.supportTypes = supportTypes;
    }
}
