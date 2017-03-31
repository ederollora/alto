package me.ollora.thesis.alto;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by s150924 on 3/30/17.
 */
public abstract class SupportedCostTypes {

    public static final CostType NUM_HOP_COUNT = new CostType("numeric", "hopcount");

    public static final CostType ORDINAL_HOP_COUNT = new CostType("ordinal", "hopcount");

    public static final CostType NUM_ROUTING_COST = new CostType("numeric", "routingcost");

    public static final CostType ORDINAL_ROUTING_COST = new CostType("ordinal", "routingcost");



    public boolean contains(CostType costType){

        Field[] fields = SupportedCostTypes.class.getDeclaredFields();

        List<Field> staticFields = new ArrayList<>();

        for(Field f : fields)
            if(Modifier.isStatic(f.getModifiers())) staticFields.add(f);

        return false;

    }

}
