package dtu.alto.pid;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by s150924 on 3/30/17.
 */
public class PIDFilter {

    @JsonProperty("srcs")
    private List<PIDName> srcs = null;

    @JsonProperty("dsts")
    private List<PIDName> dsts = null;

    public PIDFilter() {

    }

    public PIDFilter(List<PIDName> srcs, List<PIDName> dsts) {
        this.srcs = srcs;
        this.dsts = dsts;
    }

    @JsonProperty("srcs")
    public List<PIDName> getSrcs() {
        return srcs;
    }

    @JsonProperty("srcs")
    public void setSrcs(List<PIDName> srcs) {
        this.srcs = srcs;
    }

    @JsonProperty("dsts")
    public List<PIDName> getDsts() {
        return dsts;
    }

    @JsonProperty("dsts")
    public void setDsts(List<PIDName> dsts) {
        this.dsts = dsts;
    }
}
