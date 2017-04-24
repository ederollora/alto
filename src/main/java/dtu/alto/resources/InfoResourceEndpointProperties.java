package dtu.alto.resources;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import dtu.alto.endpointprop.EndpointPropertyMapData;

/**
 * Created by s150924 on 4/22/17.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "endpoint-properties"
})

public class InfoResourceEndpointProperties {

    @JsonProperty("endpoint-properties")
    private EndpointPropertyMapData endpointProperties= null;

    public void InfoResourceEndpointProperties() {}

    @JsonProperty("endpoint-properties")
    public EndpointPropertyMapData getEndpointProperties() {
        return endpointProperties;
    }

    @JsonProperty("endpoint-properties")
    public void setEndpointProperties(EndpointPropertyMapData endpointProperties) {
        this.endpointProperties = endpointProperties;
    }
}
