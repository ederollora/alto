package me.ollora.thesis.alto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Map;

/**
 * Created by s150924 on 3/31/17.
 */


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "meta",
        "resources"
})

public class InfoResourceDirectory extends ResponseEntityBase {

    @JsonProperty("resources")
    private Map<ResourceID, IRDResourceEntry> resources;


}
