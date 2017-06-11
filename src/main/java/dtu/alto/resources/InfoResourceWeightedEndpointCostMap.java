package dtu.alto.resources;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import dtu.alto.base.ResponseEntityBase;
import dtu.alto.base.ResponseMeta;
import dtu.alto.cost.CostType;
import dtu.alto.endpoint.TypedEndpointAddr;
import dtu.alto.endpointcost.EndpointCostMapData;
import dtu.alto.endpointcost.EndpointDstCosts;
import dtu.alto.endpointcost.ReqEndpointCostMap;
import dtu.alto.endpointcost.WeightedEndpointCostMapData;
import org.onlab.packet.IpAddress;
import org.onosproject.net.DeviceId;
import org.onosproject.net.Path;
import org.onosproject.net.host.HostService;
import org.onosproject.net.topology.TopologyService;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

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

    public void InfoResourceEndpointCostMap(){
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
