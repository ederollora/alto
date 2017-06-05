package dtu.alto.cdn;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by s150924 on 6/5/17.
 */
public class RankedValues implements Serializable{

    @JsonProperty("weighted-value")
    Float weightedValue = null;

    @JsonProperty("rank-position")
    Integer rankPosition = null;


}
