package dtu.alto.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "resource-id",
        "tag"
})
public class VersionTag implements Serializable {

    @JsonProperty("resource-id")
    private String resourceId;

    @JsonProperty("tag")
    private String tag;

    public VersionTag(){
        //this("default-network-map");
    }

    public VersionTag(String resourceId){

        this(resourceId, false);
    }

    public VersionTag(String resourceId, Boolean hashCode){
        this.resourceId = resourceId;
        if(hashCode)
            this.tag = String.valueOf(resourceId.hashCode());
        else
            this.tag = UUID.randomUUID().toString().replaceAll("-", "");
    }

    public VersionTag(String resourceId, String tag) throws Exception {


        this.resourceId = resourceId;

        if(!this.tagIsValid(tag))
            this.tag = UUID.randomUUID().toString().replaceAll("-", "");
        else
            this.tag = tag;
    }


    @JsonProperty("resource-id")
    public String getResourceId() {
        return resourceId;
    }

    @JsonProperty("resource-id")
    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    @JsonProperty("tag")
    public String getTag() {
        return tag;
    }

    @JsonProperty("tag")
    public void setTag(String tag){

        if(tagIsValid(tag))
            this.tag = tag;
        else
            this.tag = "non-valid-tag-default";
    }

    public Boolean tagIsValid(String tag){

        if(tag == null ||
            tag.length() == 0 ||
             tag.length() > 64)
            return false;

        for (int i = 0; i < tag.length(); i++) {
            char c = tag.charAt(i);
            if (c < 0x0021 || c > 0x007E)
                return false;
        }
        // If we reach here the tag should be valid according to the standard
        return true;
    }

}