package dtu.alto.resources;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import dtu.alto.base.EndpointAddrGroup;
import dtu.alto.base.ResponseEntityBase;
import dtu.alto.base.VersionTag;
import dtu.alto.net.NetworkMapData;
import dtu.alto.pid.PID;
import dtu.alto.rest.ReqFilteredNetworkMap;
import org.onosproject.net.Host;
import org.onosproject.net.device.DeviceService;
import org.slf4j.Logger;

import java.io.Serializable;
import java.util.*;

/**
 * Created by s150924 on 3/10/17.
 *  *
 */


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "meta",
        "network-map"
})

public class InfoResourceNetworkMap extends ResponseEntityBase implements Serializable {

    private static String defaultResourceId = "default-network-map";

    @JsonProperty("network-map")
    private NetworkMapData networkMap = null;

    public InfoResourceNetworkMap() {

        this.networkMap = new NetworkMapData();
        //this.getMeta().setVersionTag(new VersionTag(defaultResourceId));
    }

    public InfoResourceNetworkMap(NetworkMapData networkMap) {
        this.networkMap = networkMap;
    }

    public InfoResourceNetworkMap(Iterable<Host> hosts,
                                  Logger log,
                                  DeviceService deviceService) {
        this.networkMap = new NetworkMapData(hosts, log, deviceService);
        this.getMeta().setVersionTag(new VersionTag(defaultResourceId));
    }

    @JsonProperty("network-map")
    public NetworkMapData getNetworkMap() {
        return networkMap;
    }

    @JsonProperty("network-map")
    public void setNetworkMap(NetworkMapData networkMap) {
        this.networkMap = networkMap;
    }

    @JsonIgnore
    public Map<PID, List<Host>> getPIDs(){

        return this.networkMap.getPidList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InfoResourceNetworkMap)) {
            return false;
        }
        InfoResourceNetworkMap that = (InfoResourceNetworkMap) o;
        return Objects.equals(getNetworkMap(), that.getNetworkMap());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNetworkMap());
    }

    public void filterMap(ReqFilteredNetworkMap filNetMap){

        if(filNetMap.getPids().size() > 0) {

            Set<String> pidsToKeep = new HashSet<String>(filNetMap.getPids());

            Set<String> pidsToRemove = this.getNetworkMap().getData().keySet();

            pidsToRemove.removeAll(pidsToKeep);


            for (String pid : pidsToRemove) {
                this.getNetworkMap().getData().remove(pid);
            }
        }

        if( filNetMap.getAddressTypes() != null &&
                filNetMap.getAddressTypes().size() > 0){

            for(Map.Entry<String, EndpointAddrGroup> entry : this.getNetworkMap().getData().entrySet()){

                for(Map.Entry<String, ArrayList<String>> setOfIPs : entry.getValue().getEndGr().entrySet()){

                    if(setOfIPs.getKey().equalsIgnoreCase(""))
                        System.out.println();


                }

            }
        }

    }

}
