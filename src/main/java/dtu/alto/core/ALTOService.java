package dtu.alto.core;

import com.sun.corba.se.spi.activation.Server;
import dtu.alto.base.VersionTag;
import dtu.alto.cdn.RankedValues;
import dtu.alto.cdn.ServerReport;
import dtu.alto.resources.InfoResourceCostMap;
import dtu.alto.resources.InfoResourceNetworkMap;
import dtu.alto.pid.PIDName;
import org.onlab.packet.IpAddress;
import org.onosproject.net.Host;

import java.util.List;
import java.util.Map;
import java.util.Set;

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

    Map<IpAddress, RankedValues> getRankedEndpoints(List<IpAddress> endpoints);


    List<IpAddress> getContentServers();

}




