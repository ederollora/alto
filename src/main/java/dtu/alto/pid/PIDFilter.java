package dtu.alto.pid;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by s150924 on 3/30/17.
 */
public class PIDFilter {

    @JsonProperty("srcs")
    private List<String> srcs = null;

    @JsonProperty("dsts")
    private List<String> dsts = null;

    public PIDFilter() {

    }

    public PIDFilter(List<String> srcs, List<String> dsts) {
        this.srcs = srcs;
        this.dsts = dsts;
    }

    @JsonProperty("srcs")
    public List<String> getSrcs() {
        return srcs;
    }

    @JsonProperty("srcs")
    public void setSrcs(List<String> srcs) {
        this.srcs = srcs;
    }

    @JsonProperty("dsts")
    public List<String> getDsts() {
        return dsts;
    }

    @JsonProperty("dsts")
    public void setDsts(List<String> dsts) {
        this.dsts = dsts;
    }
}
