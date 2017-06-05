package dtu.alto.cdn;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.onlab.packet.IpAddress;

import java.io.Serializable;

/**
 * Created by s150924 on 6/4/17.
 */
public class ServerStatistics implements Serializable {


    @JsonProperty("ip-address")
    private IpAddress ipAddress = null;

    @JsonProperty("served-requests")
    private Integer servedRequests  = null;

    @JsonProperty("load")
    private Float load = null;

    @JsonProperty("active-clients")
    private Integer activeClients = null;

    @JsonProperty("uplink-capacity") // bytes/s
    private Integer uplinkCapacity = null;

    @JsonProperty("delay")
    private Float delay = null;

    @JsonProperty("timestamp")
    private Long statTimestapm = null;

    public ServerStatistics() {

    }

    @JsonProperty("ip-address")
    public IpAddress getIpAddress() {
        return ipAddress;
    }

    @JsonProperty("ip-address")
    public void setIpAddress(IpAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    @JsonProperty("served-requests")
    public Integer getServedRequests() {
        return servedRequests;
    }

    @JsonProperty("served-requests")
    public void setServedRequests(Integer servedRequests) {
        this.servedRequests = servedRequests;
    }

    @JsonProperty("load")
    public Float getLoad() {
        return load;
    }

    @JsonProperty("load")
    public void setLoad(Float load) {
        this.load = load;
    }

    @JsonProperty("active-clients")
    public Integer getActiveClients() {
        return activeClients;
    }

    @JsonProperty("active-clients")
    public void setActiveClients(Integer activeClients) {
        this.activeClients = activeClients;
    }

    @JsonProperty("uplink-capacity")
    public Integer getUplinkCapacity() {
        return uplinkCapacity;
    }

    @JsonProperty("uplink-capacity")
    public void setUplinkCapacity(Integer uplinkCapacity) {
        this.uplinkCapacity = uplinkCapacity;
    }

    @JsonProperty("delay")
    public Float getDelay() {
        return delay;
    }

    @JsonProperty("delay")
    public void setDelay(Float delay) {
        this.delay = delay;
    }

    @JsonProperty("timestamp")
    public Long getStatTimestapm() {
        return statTimestapm;
    }

    @JsonProperty("timestamp")
    public void setStatTimestapm(Long statTimestapm) {
        this.statTimestapm = statTimestapm;
    }
}
