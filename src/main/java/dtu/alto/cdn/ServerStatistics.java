package dtu.alto.cdn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dtu.alto.endpoint.TypedEndpointAddr;

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
    private Integer load;

    @JsonProperty("active-clients")
    private Integer activeClients = null;

    @JsonProperty("uplink-capacity") // bytes/s
    private Integer uplinkCapacity = null;

    @JsonProperty("delay")
    private Integer delay;

    @JsonProperty("timestamp")
    private Long statTimestamp = null;

    @JsonIgnore
    private double normalizedDelay;

    @JsonIgnore
    private double normalizedCapacity;

    @JsonIgnore
    private double normalizedLoad;

    @JsonIgnore
    private double normalizedAvail;

    @JsonIgnore
    private double nextClientAvailBW;

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
    public Integer getLoad() {
        return load;
    }

    @JsonProperty("load")
    public void setLoad(Integer load) {
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
    public Integer getDelay() {
        return delay;
    }

    @JsonProperty("delay")
    public void setDelay(Integer delay) {
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

    @JsonIgnore
    public double getNormalizerLoad() {
        return normalizedLoad;
    }

    @JsonIgnore
    public void setNormalizerLoad(double normalizerLoad) {
        this.normalizedLoad = normalizerLoad;
    }

    @JsonIgnore
    public double getNormalizedCapacity() {
        return normalizedCapacity;
    }

    @JsonIgnore
    public void setNormalizedCapacity(double normalizedCapacity) {
        this.normalizedCapacity = normalizedCapacity;
    }

    @JsonIgnore
    public double getNormalizedDelay() {
        return normalizedDelay;
    }

    @JsonIgnore
    public void setNormalizedDelay(double normalizedDelay) {
        this.normalizedDelay = normalizedDelay;
    }

    @JsonIgnore
    public double getNormalizedLoad() {
        return normalizedLoad;
    }

    @JsonIgnore
    public void setNormalizedLoad(double normalizedLoad) {
        this.normalizedLoad = normalizedLoad;
    }

    @JsonIgnore
    public double getNormalizedAvail() {
        return normalizedAvail;
    }

    @JsonIgnore
    public void setNormalizedAvail(double normalizedAvail) {
        this.normalizedAvail = normalizedAvail;
    }

    @JsonIgnore
    public double getNextClientAvailBW() {
        return nextClientAvailBW;
    }

    @JsonIgnore
    public void setNextClientAvailBW(double nextClientAvailBW) {
        this.nextClientAvailBW = nextClientAvailBW;
    }

    @JsonIgnore
    public double getPerClientRate(){
        return round(((double)getUplinkCapacity() / (getActiveClients()+1)),2);
    }

    private double round(double value, int places) {
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
