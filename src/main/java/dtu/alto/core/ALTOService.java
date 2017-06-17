package dtu.alto.core;

import dtu.alto.base.VersionTag;
import dtu.alto.cdn.ServerReport;
import dtu.alto.endpoint.TypedEndpointAddr;
import dtu.alto.resources.InfoResourceCostMap;
import dtu.alto.resources.InfoResourceEndpointCostMap;
import dtu.alto.resources.InfoResourceNetworkMap;
import dtu.alto.pid.PIDName;
import org.onosproject.net.Host;

import java.util.List;
import java.util.Map;

/**
 * Created by s150924 on 3/22/17.
 */
public interface ALTOService {

    InfoResourceNetworkMap getNetworkMap();

    InfoResourceCostMap getCostMap();

    Map<PIDName,List<Host>> getPIDs();

    List<VersionTag> getAllVersionTags();

    void rebuildMaps();

    void updateServerReport(ServerReport sReport);

    ServerReport getServerReport();

    void getRankedEndpoints(InfoResourceEndpointCostMap eCostMap);

    List<TypedEndpointAddr> getContentServers();

}




