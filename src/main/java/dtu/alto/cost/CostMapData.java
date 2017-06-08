package dtu.alto.cost;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import dtu.alto.endpoint.TypedEndpointAddr;
import dtu.alto.pid.PIDName;
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

    SortedMap<PIDName, DstCosts> data = new TreeMap<>();


    public CostMapData(){
        this.data = new TreeMap<>();
    }

    public CostMapData(CostMapData costData){
        this.data = new TreeMap<>(costData.getData());
    }


    @JsonAnyGetter
    public SortedMap<PIDName, DstCosts> getData() {
        return data;
    }

    @JsonAnySetter
    public void setData(SortedMap<PIDName, DstCosts> data) {
        this.data = data;
    }


    public static CostMapData numRoutingCostMap(Map<PIDName, List<Host>> pidList,
                              Logger log,
                              TopologyService topoServ){

        CostMapData cmd = new CostMapData();

        for(PIDName sourcePID: pidList.keySet()){

            for(PIDName destPID: pidList.keySet()){

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
                                cost = p.cost() - 1;
                            }
                        }
                    }

                    /*
                    *  PID1[ h1 ---- x ] ------ x ------- [ x ---- h2 ]PID2
                    *          r1       r2        r3
                    *
                    * by def, path.cost() from r1 to r3 is 2,
                    * then it counts as traversed links
                    *
                    * add 1 to cost to get the hopcount from
                    * hosts of each pid.
                    * subtract 1 to cost ot get the hopcount
                    * taking the ref switches as units of traffic source
                    *
                    *
                    * */


                    cmd.addCostToPID(sourcePID, destPID, (int)cost);
                }else{
                    cmd.addCostToPID(sourcePID, destPID, 0);
                }
            }
        }

        return cmd;
    }


    public void addCostToPID(PIDName sourcePIDName, PIDName destPIDName, int cost){

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
