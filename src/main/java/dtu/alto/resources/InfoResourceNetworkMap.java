package dtu.alto.resources;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import dtu.alto.base.EndpointAddrGroup;
import dtu.alto.base.ResponseEntityBase;
import dtu.alto.base.ResponseMeta;
import dtu.alto.base.VersionTag;
import dtu.alto.endpoint.AddressType;
import dtu.alto.endpoint.EndpointAddr;
import dtu.alto.net.NetworkMapData;
import dtu.alto.pid.PIDName;
import dtu.alto.rest.ReqFilteredNetworkMap;
import org.onosproject.net.Host;
import org.onosproject.net.device.DeviceService;
import org.onosproject.net.host.HostAdminService;
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

    public InfoResourceNetworkMap(Iterable<Host> hosts, Logger log,
                                  DeviceService deviceService, HostAdminService hostAdminService) {
        this.networkMap = new NetworkMapData(hosts, log, deviceService, hostAdminService);
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
    public Map<PIDName, List<Host>> getPIDs(){

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

        Map<String, PIDName> pids = this.networkMap.getPids();

        NetworkMapData newMapData = new NetworkMapData();

        Map<PIDName, EndpointAddrGroup> filteredData = newMapData.getData();

        if(pids != null && pids.keySet().size() > 0 && filNetMap.getPids().size() > 0){

            filNetMap.getPids().retainAll(this.getNetworkMap().getData().keySet());

            for(Map.Entry<PIDName, EndpointAddrGroup> data : this.getNetworkMap().getData().entrySet()){

                if(filNetMap.getPids().contains(data.getKey()) &&
                        !filteredData.containsKey(data.getKey())){

                    filteredData.put(data.getKey(), new EndpointAddrGroup());

                    for(Map.Entry<AddressType, List<EndpointAddr>> group : data.getValue().getEndGr().entrySet()) {
                        if (filNetMap.getAddressTypes().size() > 0){
                            for (AddressType addType : filNetMap.getAddressTypes())
                                if (group.getKey().equals(addType))
                                    filteredData.get(data.getKey()).getEndGr().put(group.getKey(), group.getValue());

                        }else{
                            filteredData.get(data.getKey()).getEndGr().put(group.getKey(), group.getValue());
                        }
                    }
                }

            }

        }

        this.networkMap = newMapData;

    }

    public InfoResourceNetworkMap newInstance(){
        // new copy, not a reference
        return new InfoResourceNetworkMap(
                this.getMeta(), this.getNetworkMap()
        );
    }

}
