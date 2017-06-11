package dtu.alto.cost;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import dtu.alto.endpoint.TypedEndpointAddr;
import dtu.alto.endpointcost.EndpointDstCosts;
import dtu.alto.pid.PIDName;
import org.onosproject.net.Host;
import org.onosproject.net.Path;
import org.onosproject.net.topology.TopologyService;
import org.slf4j.Logger;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by s150924 on 3/15/17.
 */
public class CostMapData implements Serializable{

    SortedMap<PIDName, DstCosts> data = new TreeMap<>();

    @JsonIgnore
    Map<String, SortedMap<PIDName, DstCosts>> modes = new HashMap<>();

    @JsonIgnore
    Map<TypedEndpointAddr, Double> normalizedMap = null;

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

                /*
                *  PID1[ h1 ---- x ] ------ x ------- [ x ---- h2 ]PID2
                *          r1       r2        r3
                *
                * by def, path.cost() from r1 to r3 is 2,
                * then it counts as traversed links
                *
                * add 2 to cost to get the hopcount from
                * hosts to host.
                * subtract 1 to cost ot get the hopcount
                * taking the ref switches as units of traffic source
                *
                *
                * */


                cmd.addCostToPID(sourcePID, destPID, (int)cost + 2);

            }
        }

        return cmd;
    }

    public static CostMapData hopCountCostMap(Map<PIDName, List<Host>> pidList,
                                                Logger log,
                                                TopologyService topoServ){

        CostMapData cmd = new CostMapData();

        for(PIDName sourcePID: pidList.keySet()){

            for(PIDName destPID: pidList.keySet()){

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

                cmd.addCostToPID(sourcePID, destPID, (int)cost + 1);

            }
        }

        return cmd;
    }

    public static CostMapData createOrdinalMap(CostMapData numMap){

        CostMapData ordMap = new CostMapData();

        int i = 1;

        for(Map.Entry<PIDName,DstCosts> pidtodst : numMap.data.entrySet()) {

            SortedMap<PIDName,DstCosts> newpitodst = ordMap.getData();
            newpitodst.put(pidtodst.getKey(), new DstCosts());

            SortedMap<PIDName, Integer> dstcosts= pidtodst.getValue().getDstCosts();

            for(Map.Entry<PIDName,Integer> costtopid : sortByValue(dstcosts).entrySet()) {
                newpitodst.get(pidtodst.getKey()).getDstCosts().put(costtopid.getKey(), i);
                i++;
            }

            i = 1;
        }

        return ordMap;

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


    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(/*Collections.reverseOrder()*/))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

}
