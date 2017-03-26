package me.ollora.thesis.alto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.onosproject.net.topology.Topology;
import org.onosproject.net.topology.TopologyService;
import org.slf4j.Logger;

import java.util.ArrayList;

/**
 * Created by s150924 on 3/15/17.
 */


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "meta",
        "cost-map"
})


public class InfoResourceCostMap extends ResponseEntityBase{

    @JsonProperty("cost-map")
    private CostMapData costMap;

    public InfoResourceCostMap(){

        this.costMap = new CostMapData();
    }

    public InfoResourceCostMap(CostMapData costMap) {

        this.costMap = costMap;
    }

    public InfoResourceCostMap(ResponseMeta meta, CostMapData costMap) {

        super(meta);
        this.costMap = costMap;
    }

    public InfoResourceCostMap(InfoResourceNetworkMap infoResNetMap,
                               Logger log,
                               TopologyService topoServ) {

        this.costMap = new CostMapData(infoResNetMap.getPIDs(), log, topoServ);
        this.getMeta().setDependentVersionTags(new ArrayList<>());
        this.getMeta().getDependentVersionTags().add(infoResNetMap.VersionTag());
        this.getMeta().setCostType(new CostType("numerical","hopcount"));

    }

    @JsonProperty("cost-map")
    public CostMapData getCostMap() {
        return costMap;
    }

    @JsonProperty("cost-map")
    public void setCostMap(CostMapData costMap) {
        this.costMap = costMap;
    }



}
