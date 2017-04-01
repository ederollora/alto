package dtu.alto.core;

import dtu.alto.base.VersionTag;
import dtu.alto.resources.InfoResourceCostMap;
import dtu.alto.resources.InfoResourceNetworkMap;
import dtu.alto.pid.PID;
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

    List<VersionTag> getAllVersionTags();

    void rebuildMaps();

}




