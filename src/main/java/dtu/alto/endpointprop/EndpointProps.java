package dtu.alto.endpointprop;

import java.io.Serializable;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by s150924 on 4/22/17.
 */
public class EndpointProps implements Serializable {

    SortedMap<EndpointPropertyType, String> dstCosts = new TreeMap<>();
}
