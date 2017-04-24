package dtu.alto.endpointprop;

import com.fasterxml.jackson.annotation.JsonProperty;
import dtu.alto.endpoint.TypedEndpointAddr;

import java.util.List;

/**
 * Created by s150924 on 4/22/17.
 */
public class ReqEndpointProp {

    @JsonProperty("properties")
    List<EndpointPropertyType> properties = null;

    @JsonProperty("endpoints")
    List<TypedEndpointAddr> endpoints = null;

    public ReqEndpointProp(){

    }

    @JsonProperty("properties")
    public List<EndpointPropertyType> getProperties() {
        return properties;
    }

    @JsonProperty("properties")
    public void setProperties(List<EndpointPropertyType> properties) {
        this.properties = properties;
    }

    @JsonProperty("endpoints")
    public List<TypedEndpointAddr> getEndpoints() {
        return endpoints;
    }

    @JsonProperty("endpoints")
    public void setEndpoints(List<TypedEndpointAddr> endpoints) {
        this.endpoints = endpoints;
    }
}


