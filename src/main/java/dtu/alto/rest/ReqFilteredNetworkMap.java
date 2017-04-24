package dtu.alto.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import dtu.alto.pid.PIDName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by s150924 on 3/28/17.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "pids",
        "address-types"
})
public class ReqFilteredNetworkMap implements Serializable {

    @JsonProperty("pids")
    List<PIDName> pids = new ArrayList<>();

    @JsonProperty("address-types")
    List<String> addressType = new ArrayList<>();

    public ReqFilteredNetworkMap(){

    }

    public ReqFilteredNetworkMap(List<PIDName> pids, List<String> addressType) {
        this.pids = pids;
        this.addressType = addressType;
    }

    @JsonProperty("pids")
    public List<PIDName> getPids() {
        return pids;
    }

    @JsonProperty("pids")
    public void setPids(List<PIDName> pids) {
        this.pids = pids;
    }

    @JsonProperty("address-types")
    public List<String> getAddressTypes() {
        return addressType;
    }

    @JsonProperty("address-types")
    public void setAddressTypes(List<String> addressType) {
        this.addressType = addressType;
    }
}
