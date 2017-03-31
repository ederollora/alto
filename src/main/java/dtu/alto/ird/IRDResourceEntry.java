package dtu.alto.ird;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import dtu.alto.base.Capabilities;
import dtu.alto.base.ResourceID;

import java.util.List;

/**
 * Created by s150924 on 3/31/17.
 */


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "uri",
        "media-type",
        "accepts",
        "capabilities",
        "uses"
})

public class IRDResourceEntry {


    @JsonProperty("uri")
    private String uri;

    @JsonProperty("media-type")
    private String mediaType;

    @JsonProperty("accepts")
    private String accepts;

    @JsonProperty("capabilities")
    private Capabilities capabilities;

    @JsonProperty("uses")
    private List<ResourceID> uses;



    @JsonProperty("uri")
    public String getUri() {
        return uri;
    }

    @JsonProperty("uri")
    public void setUri(String uri) {
        this.uri = uri;
    }

    @JsonProperty("media-type")
    public String getMediaType() {
        return mediaType;
    }

    @JsonProperty("media-type")
    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    @JsonProperty("accepts")
    public String getAccepts() {
        return accepts;
    }

    @JsonProperty("accepts")
    public void setAccepts(String accepts) {
        this.accepts = accepts;
    }

    @JsonProperty("capabilities")
    public Capabilities getCapabilities() {
        return capabilities;
    }

    @JsonProperty("capabilities")
    public void setCapabilities(Capabilities capabilities) {
        this.capabilities = capabilities;
    }

    @JsonProperty("uses")
    public List<ResourceID> getUses() {
        return uses;
    }

    @JsonProperty("uses")
    public void setUses(List<ResourceID> uses) {
        this.uses = uses;
    }
}
