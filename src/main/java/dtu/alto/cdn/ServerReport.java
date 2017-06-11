package dtu.alto.cdn;

import com.fasterxml.jackson.annotation.JsonProperty;
import dtu.alto.endpoint.TypedEndpointAddr;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by s150924 on 6/5/17.
 */
public class ServerReport implements Serializable {

    @JsonProperty("servers")
    private HashMap<TypedEndpointAddr, ServerStatistics> serverStats = null;

    @JsonProperty("timestamp")
    private Long timeStamp = null;



    public ServerReport() {
        serverStats = new HashMap<>();
        timeStamp = Integer.toUnsignedLong(0);
    }

    public ServerReport(HashMap<TypedEndpointAddr, ServerStatistics> serverStats, Long timeStamp) {
        this.serverStats = serverStats;
        this.timeStamp = timeStamp;
    }

    @JsonProperty("servers")
    public HashMap<TypedEndpointAddr, ServerStatistics> getServerStats() {
        return serverStats;
    }

    @JsonProperty("servers")
    public void setServerStats(HashMap<TypedEndpointAddr, ServerStatistics> serverStats) {
        this.serverStats = serverStats;
    }

    @JsonProperty("timestamp")
    public Long getTimeStamp() {
        return timeStamp;
    }

    @JsonProperty("timestamp")
    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }


    @Override
    public String toString() {
        return "Servers: "+serverStats.keySet().size()
               +", first server: "+serverStats.entrySet().iterator().next().getKey()
               +", bw: "+serverStats.entrySet().iterator().next().getValue().getUplinkCapacity()
               +", delay: "+serverStats.entrySet().iterator().next().getValue().getDelay();
    }
}
