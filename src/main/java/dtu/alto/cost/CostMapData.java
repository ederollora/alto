package dtu.alto.cost;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import dtu.alto.pid.PID;
import dtu.alto.base.SupportedCostTypes;
import org.onosproject.net.Host;
import org.onosproject.net.Path;
import org.onosproject.net.topology.TopologyService;
import org.slf4j.Logger;

import java.io.Serializable;
import java.util.*;

/**
 * Created by s150924 on 3/15/17.
 */
public class CostMapData implements Serializable{

    SortedMap<String, DstCosts> data = new TreeMap<String, DstCosts>();

    @JsonIgnore
    private Map<String, DstCosts> unorderedData = null;

    public CostMapData(){
        this.data = new TreeMap<>();
    }

    public CostMapData(SortedMap<String, DstCosts> data) {
        this.data = data;
    }

    public CostMapData(SortedMap<PID, List<Host>> pidList, Logger log,
                       TopologyService topoServ, CostType costType){
        this();

        if(costType.equals(SupportedCostTypes.NUM_HOP_COUNT))
            this.numHopCountCostMap(pidList, log, topoServ);

    }


    @JsonAnyGetter
    public SortedMap<String, DstCosts> getData() {
        return data;
    }

    @JsonAnySetter
    public void setData(SortedMap<String, DstCosts> data) {
        this.data = data;
    }


    public static CostMapData numHopCountCostMap(Map<PID, List<Host>> pidList,
                              Logger log,
                              TopologyService topoServ){

        CostMapData cmd = new CostMapData();

        for(PID sourcePID: pidList.keySet()){

            for(PID destPID: pidList.keySet()){

                if(!sourcePID.equals(destPID)){

                    Set<Path> paths = topoServ.getPaths(
                            topoServ.currentTopology(),
                            sourcePID.getPidRefSwitch().id(),
                            destPID.getPidRefSwitch().id()
                    );

                    double cost = 0;

                    if(paths.size() > 0){
                        for(Path p: paths){
                            if(cost == 0 || p.cost() < cost){
                                cost = p.cost();
                            }
                        }
                    }
                    cmd.addCostToPID(sourcePID.getPidName(), destPID.getPidName(), (int)cost);

                }else{
                    cmd.addCostToPID(sourcePID.getPidName(), destPID.getPidName(), 1);
                }
            }
        }

        return cmd;
    }


    public void addCostToPID(String sourcePIDName, String destPIDName, int cost){

        if(this.data == null){
            this.data = new TreeMap<>();
        }

        if(this.data.containsKey(sourcePIDName)) {
            this.data.get(sourcePIDName).insertCost(destPIDName, cost);
        }else{
            this.data.put(sourcePIDName, new DstCosts(destPIDName, cost));
        }
    }




}
