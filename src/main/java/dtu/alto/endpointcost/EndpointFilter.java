package dtu.alto.endpointcost;

import com.fasterxml.jackson.annotation.JsonProperty;
import dtu.alto.endpoint.TypedEndpointAddr;

import java.util.List;

/**
 * Created by s150924 on 4/23/17.
 */
public class EndpointFilter {

    @JsonProperty("srcs")
    List<TypedEndpointAddr> srcs = null;

    @JsonProperty("dsts")
    List<TypedEndpointAddr> dsts = null;

    public void EndpointFilter(){}

    @JsonProperty("srcs")
    public List<TypedEndpointAddr> getSrcs() {
        return srcs;
    }

    @JsonProperty("srcs")
    public void setSrcs(List<TypedEndpointAddr> srcs) {
        this.srcs = srcs;
    }

    @JsonProperty("dsts")
    public List<TypedEndpointAddr> getDsts() {
        return dsts;
    }

    @JsonProperty("dsts")
    public void setDsts(List<TypedEndpointAddr> dsts) {
        this.dsts = dsts;
    }
}
