package dtu.alto.cdn;

import com.fasterxml.jackson.annotation.JsonProperty;
import dtu.alto.endpoint.TypedEndpointAddr;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by s150924 on 6/5/17.
 */
public class ServerReport implements Serializable {

    @JsonProperty("servers")
    private Map<TypedEndpointAddr, ServerStatistics> serverStats = null;

    @JsonProperty("timestamp")
    private Long timeStamp = null;

    public ServerReport() {
        serverStats = new HashMap<>();
        timeStamp = Integer.toUnsignedLong(0);
    }

    public ServerReport(Map<TypedEndpointAddr, ServerStatistics> serverStats, Long timeStamp) {
        this.serverStats = serverStats;
        this.timeStamp = timeStamp;
    }

    @JsonProperty("servers")
    public Map<TypedEndpointAddr, ServerStatistics> getServerStats() {
        return serverStats;
    }

    @JsonProperty("servers")
    public void setServerStats(Map<TypedEndpointAddr, ServerStatistics> serverStats) {
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

    public void computeNormalizedValues(){

        int maxBW = Integer.MIN_VALUE;
        int minBW = Integer.MAX_VALUE;

        double maxPerClient = Double.MIN_NORMAL;
        double minPerClient = Double.MAX_VALUE;

        for (Map.Entry<TypedEndpointAddr, ServerStatistics> entry : this.getServerStats().entrySet()){

            if(entry.getValue().getUplinkCapacity() > maxBW)
                maxBW = entry.getValue().getUplinkCapacity().intValue();

            if(entry.getValue().getUplinkCapacity() < minBW)
                minBW = entry.getValue().getUplinkCapacity().intValue();

            if(entry.getValue().getPerClientRate() > maxPerClient)
                maxPerClient = entry.getValue().getPerClientRate();

            if(entry.getValue().getPerClientRate() < minPerClient)
                minPerClient = entry.getValue().getPerClientRate();
        }

        for (Map.Entry<TypedEndpointAddr, ServerStatistics> entry : this.getServerStats().entrySet()){

            int bw_num = entry.getValue().getUplinkCapacity() - minBW;
            int bw_den = maxBW - minBW;

            double normBW;

            if (maxBW == minBW)
                normBW = 0.5;
            else
                normBW = ((double) bw_num) / bw_den;

            entry.getValue().setNormalizedCapacity(normBW);

            double perclient_num = entry.getValue().getPerClientRate() - minPerClient;
            double perclient_den = maxPerClient - minPerClient;

            double normPerClient;

            if (maxPerClient == minPerClient)
                normPerClient = 0.5;
            else
                normPerClient = perclient_num / perclient_den;

            entry.getValue().setNextClientAvailBW(entry.getValue().getPerClientRate());
            entry.getValue().setNormalizedAvail(normPerClient);

        }
    }
}
