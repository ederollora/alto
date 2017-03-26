package me.ollora.thesis.alto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by s150924 on 3/14/17.
 */
public class CostType implements Serializable {

    @JsonProperty("cost-metric")
    private String costMetric = null;

    @JsonProperty("cost-mode")
    private String costMode = null;

    private String description = null;

    public CostType(){

    }

    public CostType(String costMetric, String costMode){
        this.setCostMetric(costMetric);
        this.setCostMode(costMode);
    }

    @JsonProperty("cost-metric")
    public String getCostMetric() {
        return costMetric;
    }

    @JsonProperty("cost-metric")
    public void setCostMetric(String costMetric) {
        this.costMetric = costMetric;
    }

    @JsonProperty("cost-mode")
    public String getCostMode() {
        return costMode;
    }

    @JsonProperty("cost-mode")
    public void setCostMode(String costMode) {
        this.costMode = costMode;
    }

    @JsonIgnore
    public String getDescription() {
        return description;
    }
    @JsonIgnore
    public void setDescription(String description) {
        this.description = description;
    }


}
