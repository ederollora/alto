package dtu.alto.cost;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by s150924 on 3/14/17.
 */
public class CostType implements Serializable {

    @JsonProperty("cost-mode")
    private String costMode = null;

    @JsonProperty("cost-metric")
    private String costMetric = null;

    private String description = null;

    public CostType(){

    }

    public CostType(String costMode, String costMetric){
        this.setCostMode(costMode);
        this.setCostMetric(costMetric);
    }

    public CostType(CostType cType){
        this(cType.getCostMode(), cType.getCostMetric());

    }

    @JsonProperty("cost-mode")
    public String getCostMode() {
        return costMode;
    }

    @JsonProperty("cost-mode")
    public void setCostMode(String costMode) {
        this.costMode = costMode;
    }

    @JsonProperty("cost-metric")
    public String getCostMetric() {
        return costMetric;
    }

    @JsonProperty("cost-metric")
    public void setCostMetric(String costMetric) {
        this.costMetric = costMetric;
    }

    @JsonIgnore
    public String getDescription() {
        return description;
    }

    @JsonIgnore
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CostType costType = (CostType) o;

        if (!costMetric.equals(costType.costMetric)) return false;
        return costMode.equals(costType.costMode);
    }

    @Override
    public int hashCode() {

        int result = costMetric.hashCode();
        result = 31 * result + costMode.hashCode();
        return result;
    }
}
