package dtu.alto.core;


import dtu.alto.endpoint.TypedEndpointAddr;
import dtu.alto.linkload.PortStats;

import java.util.HashMap;

/**
 * Created by s150924 on 6/5/17.
 */
public interface LoadCheckService {

    HashMap<TypedEndpointAddr, PortStats> getLoadReport();

}
