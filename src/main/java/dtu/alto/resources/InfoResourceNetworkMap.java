package dtu.alto.resources;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import dtu.alto.base.EndpointAddrGroup;
import dtu.alto.base.ResponseEntityBase;
import dtu.alto.base.ResponseMeta;
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

    }


    public InfoResourceNetworkMap(ResponseMeta rMeta, NetworkMapData nData) {

        super(rMeta);
        this.networkMap = new NetworkMapData(nData);
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

    @Override
    public Object clone(){
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void filterMap(ReqFilteredNetworkMap filNetMap){

        Map<String, PID> pids = this.networkMap.getPids();

        NetworkMapData newMapData = new NetworkMapData();

        Map<String, EndpointAddrGroup> filteredData = newMapData.getData();

        if(pids != null && pids.keySet().size() > 0 && filNetMap.getPids().size() > 0){

            filNetMap.getPids().retainAll(this.getNetworkMap().getData().keySet());

            for(Map.Entry<String, EndpointAddrGroup> data : this.getNetworkMap().getData().entrySet()){

                if(filNetMap.getPids().contains(data.getKey()) &&
                        !filteredData.containsKey(data.getKey())){

                    filteredData.put(data.getKey(), new EndpointAddrGroup());

                    for(Map.Entry<String, ArrayList<String>> group : data.getValue().getEndGr().entrySet())
                        filteredData.get(data.getKey()).getEndGr().put(group.getKey(), group.getValue());

                }

            }

        }

        /*if(filNetMap.getAddressTypes() != null){
            //filNetMap.getAddressTypes().size() > 0
            for(Map.Entry<String, EndpointAddrGroup> entry : this.getNetworkMap().getData().entrySet()){

                for(Map.Entry<String, ArrayList<String>> setOfIPs : entry.getValue().getEndGr().entrySet()){

                    if(setOfIPs.getKey().equalsIgnoreCase(""))
                        System.out.println();
                }
            }
        }*/

        this.networkMap = newMapData;

    }


    public InfoResourceNetworkMap newInstance(){
        // new copy, not a reference
        return new InfoResourceNetworkMap(
                this.getMeta(), this.getNetworkMap()
        );
    }

}
