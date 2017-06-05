package dtu.alto.cdn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dtu.alto.cost.CostType;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

/**
 * Created by s150924 on 6/5/17.
 */
public class ServerReport implements Serializable {

    @JsonProperty("servers")
    private List<ServerStatistics> serverStats = null;

    @JsonProperty("servers")
    public List<ServerStatistics> getServerStats() {
        return serverStats;
    }

    @JsonProperty("servers")
    public void setServerStats(List<ServerStatistics> serverStats) {
        this.serverStats = serverStats;
    }


}
