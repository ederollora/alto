package dtu.alto.endpointprop;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import dtu.alto.endpoint.TypedEndpointAddr;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by s150924 on 4/22/17.
 */
public class EndpointPropertyMapData {

    SortedMap<TypedEndpointAddr, EndpointProps> endpointpropdata = new TreeMap<>();

    @JsonAnyGetter
    public SortedMap<TypedEndpointAddr, EndpointProps> getEndpointpropdata() {
        return endpointpropdata;
    }

    @JsonAnyGetter
    public void setEndpointpropdata(SortedMap<TypedEndpointAddr, EndpointProps> endpointpropdata) {
        this.endpointpropdata = endpointpropdata;
    }
}
