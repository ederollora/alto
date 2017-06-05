package dtu.alto.core;


import dtu.alto.linkload.PortStats;
import org.onlab.packet.IpAddress;

import java.util.HashMap;

/**
 * Created by s150924 on 6/5/17.
 */
public interface LoadCheckService {

    HashMap<IpAddress, PortStats> getLoadReport();

}
