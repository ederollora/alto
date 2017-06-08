package dtu.alto.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import dtu.alto.cost.CostType;
import dtu.alto.ird.IRDMetaCostTypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "versionTag",
        "dependent-vtags",
        "cost-type",
        "cost-types",
        "default-alto-network-map",
        "code",
        "syntax-error",
        "field",
        "value"
})


public class ResponseMeta implements Serializable {

    @JsonIgnore
    public int i = 0;

    @JsonProperty("vtag")
    private VersionTag vtag = null;

    @JsonProperty("dependent-vtags")
    private List<VersionTag> dependentVersionTags = null;

    @JsonProperty("cost-type")
    private CostType costType = null;

    @JsonProperty("cost-types")
    private IRDMetaCostTypes costTypes = null;

    @JsonProperty("default-alto-network-map")
    private String defaultAltoNetworkMap = null;


    public ResponseMeta(){

    }

    public ResponseMeta(String resourceId){
        this.vtag = new VersionTag(resourceId);

    }

    public ResponseMeta(String resourceId, String vtag){
        this.vtag.setResourceId(resourceId);
        this.vtag.setTag(vtag);

    }

    public ResponseMeta(CostType cType){
        this.costType = cType;
    }

    public ResponseMeta(ResponseMeta rMeta){

        if(rMeta.getVersionTag() != null)
            this.vtag = new VersionTag(rMeta.getVersionTag());

        if(rMeta.getDependentVersionTags() != null)
            this.dependentVersionTags = rMeta.getDependentVersionTags();

        if(rMeta.getCostType() != null)
            this.costType = new CostType(rMeta.getCostType());

        if(rMeta.costTypes != null)
            this.costTypes = new IRDMetaCostTypes(rMeta.getCostTypes());

        if(rMeta.getDefaultAltoNetworkMap() != null)
            this.defaultAltoNetworkMap = new String(rMeta.getDefaultAltoNetworkMap());

    }

    @JsonProperty("vtag")
    public VersionTag getVersionTag() {

        return vtag;
    }

    @JsonProperty("vtag")
    public void setVersionTag(VersionTag vtag) {

        this.vtag = vtag;
    }

    @JsonProperty("dependent-vtags")
    public List<VersionTag> getDependentVersionTags() {

        return dependentVersionTags;
    }

    @JsonProperty("dependent-vtags")
    public void setDependentVersionTags(List<VersionTag> dependentVersionTags) {
        this.dependentVersionTags = dependentVersionTags;
    }

    @JsonProperty("cost-types")
    public IRDMetaCostTypes getCostTypes() {

        return costTypes;
    }

    @JsonProperty("cost-types")
    public void setCostTypes(IRDMetaCostTypes costTypes) {
        this.costTypes = costTypes;
    }

    @JsonProperty("default-alto-network-map")
    public String getDefaultAltoNetworkMap() {

        return defaultAltoNetworkMap;
    }

    @JsonProperty("default-alto-network-map")
    public void setDefaultAltoNetworkMap(String defaultAltoNetworkMap) {
        this.defaultAltoNetworkMap = defaultAltoNetworkMap;
    }

    @JsonProperty("cost-type")
    public CostType getCostType() {
        return costType;
    }

    @JsonProperty("cost-type")
    public void setCostType(CostType costType) {
        this.costType = costType;
    }



    public void addDependentVtag(VersionTag dependentVtag){
        this.dependentVersionTags.add(dependentVtag);

    }

}