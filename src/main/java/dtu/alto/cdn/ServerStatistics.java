package dtu.alto.cdn;

import com.fasterxml.jackson.annotation.JsonProperty;
import dtu.alto.endpoint.TypedEndpointAddr;
import org.onlab.packet.IpAddress;

import java.io.Serializable;

/**
 * Created by s150924 on 6/4/17.
 */
public class ServerStatistics implements Serializable {


    @JsonProperty("ip-address")
    private TypedEndpointAddr ipAddress = null;

    @JsonProperty("served-requests")
    private Integer servedRequests  = null;

    @JsonProperty("load")
    private Double load = null;

    @JsonProperty("normLoad")
    private Double normalizerLoad = null;

    @JsonProperty("active-clients")
    private Integer activeClients = null;

    @JsonProperty("uplink-capacity") // bytes/s
    private Integer uplinkCapacity = null;

    @JsonProperty("normCapacity")
    private Double normalizedCapacity = null;

    @JsonProperty("delay")
    private Double delay = null;

    @JsonProperty("normDelay")
    private Double normalizedDelay = null;

    @JsonProperty("timestamp")
    private Long statTimestamp = null;

    public ServerStatistics() {

    }

    @JsonProperty("ip-address")
    public TypedEndpointAddr getIpAddress() {
        return ipAddress;
    }

    @JsonProperty("ip-address")
    public void setIpAddress(TypedEndpointAddr ipAddress) {
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
    public Double getLoad() {
        return load;
    }

    @JsonProperty("load")
    public void setLoad(Double load) {
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
    public Double getDelay() {
        return delay;
    }

    @JsonProperty("delay")
    public void setDelay(Double delay) {
        this.delay = delay;
    }

    @JsonProperty("timestamp")
    public Long getStatTimestamp() {
        return statTimestamp;
    }

    @JsonProperty("timestamp")
    public void setStatTimestamp(Long statTimestamp) {
        this.statTimestamp = statTimestamp;
    }

    @JsonProperty("normLoad")
    public Double getNormalizerLoad() {
        return normalizerLoad;
    }

    @JsonProperty("normLoad")
    public void setNormalizerLoad(Double normalizerLoad) {
        this.normalizerLoad = normalizerLoad;
    }

    @JsonProperty("normCapacity")
    public Double getNormalizedCapacity() {
        return normalizedCapacity;
    }

    @JsonProperty("normCapacity")
    public void setNormalizedCapacity(Double normalizedCapacity) {
        this.normalizedCapacity = normalizedCapacity;
    }

    @JsonProperty("normDelay")
    public Double getNormalizedDelay() {
        return normalizedDelay;
    }

    @JsonProperty("normDelay")
    public void setNormalizedDelay(Double normalizedDelay) {
        this.normalizedDelay = normalizedDelay;
    }
}
