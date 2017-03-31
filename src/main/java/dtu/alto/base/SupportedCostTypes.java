package dtu.alto.base;

import dtu.alto.cost.CostType;

/**
 * Created by s150924 on 3/30/17.
 */
public class SupportedCostTypes {

    public static final CostType NUM_HOP_COUNT = new CostType("numerical", "hopcount");

    public static final CostType ORDINAL_HOP_COUNT = new CostType("ordinal", "hopcount");

    public static final CostType NUM_ROUTING_COST = new CostType("numerical", "routingcost");

    public static final CostType ORDINAL_ROUTING_COST = new CostType("ordinal", "routingcost");


}
