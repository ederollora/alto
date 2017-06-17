package dtu.alto.resources;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import dtu.alto.base.ResponseEntityBase;
import dtu.alto.endpointcost.WeightedEndpointCostMapData;

/**
 * Created by s150924 on 4/23/17.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "meta",
        "endpoint-cost-map"
})

public class InfoResourceWeightedEndpointCostMap extends ResponseEntityBase {

    @JsonProperty("weighted-endpoint-cost-map")
    WeightedEndpointCostMapData weightedEndpointCostMapData = null;

    @JsonIgnore
    int maxValue = Integer.MIN_VALUE;

    @JsonIgnore
    int minValue = Integer.MAX_VALUE;

    public InfoResourceWeightedEndpointCostMap(){
        this.weightedEndpointCostMapData = new WeightedEndpointCostMapData();
    }

    @JsonProperty("weighted-endpoint-cost-map")
    public WeightedEndpointCostMapData getEndpointCostMap() {
        return weightedEndpointCostMapData;
    }

    @JsonProperty("weighted-endpoint-cost-map")
    public void setEndpointCostMap(WeightedEndpointCostMapData endpointCostMap) {
        this.weightedEndpointCostMapData = endpointCostMap;
    }


}
