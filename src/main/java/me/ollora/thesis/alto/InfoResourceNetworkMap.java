package me.ollora.thesis.alto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.onosproject.net.Host;
import org.onosproject.net.device.DeviceService;
import org.slf4j.Logger;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by s150924 on 3/10/17.
 *
 */


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "meta",
        "network-map"
})

public class InfoResourceNetworkMap extends ResponseEntityBase implements Serializable {

    private static String defaultResourceId = "default-network-map";

    @JsonProperty("network-map")
    private NetworkMapData networkMap;

    public InfoResourceNetworkMap() {

        this.networkMap = new NetworkMapData();
        this.getMeta().setVersionTag(new VersionTag(defaultResourceId));
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



}
