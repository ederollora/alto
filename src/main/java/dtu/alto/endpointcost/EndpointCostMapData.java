package dtu.alto.endpointcost;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import dtu.alto.endpoint.TypedEndpointAddr;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Created by s150924 on 4/23/17.
 */
public class EndpointCostMapData {


    SortedMap<TypedEndpointAddr, EndpointDstCosts> endPointCostMap = null;


    public EndpointCostMapData(){
        this.endPointCostMap = new TreeMap<>();
    }

    @JsonAnyGetter
    public SortedMap<TypedEndpointAddr, EndpointDstCosts> getEndPointCostMap() {
        return endPointCostMap;
    }

    @JsonAnySetter
    public void setEndPointCostMap(SortedMap<TypedEndpointAddr, EndpointDstCosts> endPointCostMap) {
        this.endPointCostMap = endPointCostMap;
    }


    public static EndpointCostMapData createOrdinalMap(EndpointCostMapData eCostMapData){

        EndpointCostMapData ecmd = new EndpointCostMapData();

        int i = 1;

        for(Map.Entry<TypedEndpointAddr, EndpointDstCosts> eToe : eCostMapData.endPointCostMap.entrySet()) {

            SortedMap<TypedEndpointAddr, EndpointDstCosts> newetoe = ecmd.getEndPointCostMap();

            newetoe.put(eToe.getKey(), new EndpointDstCosts());

            SortedMap<TypedEndpointAddr, Integer> dstcosts= eToe.getValue().getDstCosts();

            for(Map.Entry<TypedEndpointAddr, Integer> costToEnd : sortByValue(dstcosts).entrySet()) {
                newetoe.get(eToe.getKey()).getDstCosts().put(costToEnd.getKey(), i);
                i++;
            }

            i = 1;
        }

        return ecmd;

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
