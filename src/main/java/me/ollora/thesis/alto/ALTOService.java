package me.ollora.thesis.alto;

import org.onosproject.net.Host;

import java.util.List;
import java.util.Map;

/**
 * Created by s150924 on 3/22/17.
 */
public interface ALTOService {

    InfoResourceNetworkMap getNetworkMap();

    InfoResourceCostMap getCostMap();

    Map<PID,List<Host>> getPIDs();

    Map<String, DstCosts> getCostData();

    List<VersionTag> getAllVersionTags();

}




