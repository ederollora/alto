package me.ollora.thesis.alto;

import com.fasterxml.jackson.annotation.JsonProperty;


import java.util.List;

/**
 * Created by s150924 on 3/30/17.
 */
public class ReqFilteredCostMap {

    @JsonProperty("cost-type")
    CostType costType = null;

    @JsonProperty("constraints")
    List<String> constraints = null;

    @JsonProperty("pids")
    PIDFilter pids = null;

    public ReqFilteredCostMap(){

    }

    public ReqFilteredCostMap(CostType costType, List<String> constraints, PIDFilter pids) {
        this.costType = costType;
        this.constraints = constraints;
        this.pids = pids;
    }

    @JsonProperty("cost-type")
    public CostType getCostType() {
        return costType;
    }

    @JsonProperty("cost-type")
    public void setCostType(CostType costType) {
        this.costType = costType;
    }

    @JsonProperty("constraints")
    public List<String> getConstraints() {
        return constraints;
    }

    @JsonProperty("constraints")
    public void setConstraints(List<String> constraints) {
        this.constraints = constraints;
    }

    @JsonProperty("pids")
    public PIDFilter getPids() {
        return pids;
    }

    @JsonProperty("pids")
    public void setPids(PIDFilter pids) {
        this.pids = pids;
    }
}

