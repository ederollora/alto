package dtu.alto.endpointcost;

import com.fasterxml.jackson.annotation.JsonProperty;
import dtu.alto.cost.CostType;

import java.io.Serializable;
import java.util.List;

/**
 * Created by s150924 on 4/23/17.
 */
public class ReqEndpointCostMap implements Serializable{

    @JsonProperty("cost-type")
    CostType costType = null;

    @JsonProperty("endpoints")
    EndpointFilter endpoints = null;

    @JsonProperty("constraints")
    List<String> constraints = null;

    @JsonProperty("cost-type")
    public CostType getCostType() {
        return costType;
    }

    @JsonProperty("cost-type")
    public void setCostType(CostType costType) {
        this.costType = costType;
    }

    @JsonProperty("endpoints")
    public EndpointFilter getEndpoints() {
        return endpoints;
    }

    @JsonProperty("endpoints")
    public void setEndpoints(EndpointFilter endpoints) {
        this.endpoints = endpoints;
    }

    @JsonProperty("constraints")
    public List<String> getConstraints() {
        return constraints;
    }

    @JsonProperty("constraints")
    public void setConstraints(List<String> constraints) {
        this.constraints = constraints;
    }

}
