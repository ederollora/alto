package me.ollora.thesis.alto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

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
    List<String> pids = new ArrayList<>();

    @JsonProperty("address-types")
    List<String> addressType = new ArrayList<>();

    public ReqFilteredNetworkMap(){

    }

    public ReqFilteredNetworkMap(List<String> pids, List<String> addressType) {
        this.pids = pids;
        this.addressType = addressType;
    }

    @JsonProperty("pids")
    public List<String> getPids() {
        return pids;
    }

    @JsonProperty("pids")
    public void setPids(List<String> pids) {
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
