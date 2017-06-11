package dtu.alto.endpointcost;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import dtu.alto.endpoint.TypedEndpointAddr;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Created by s150924 on 4/23/17.
 */
public class WeightedEndpointCostMapData {


    SortedMap<TypedEndpointAddr, WeightedEndpointDstCosts> weightedEndpointDstCostsSortedMap = null;


    public WeightedEndpointCostMapData(){
        this.weightedEndpointDstCostsSortedMap = new TreeMap<>();
    }

    @JsonAnyGetter
    public SortedMap<TypedEndpointAddr, WeightedEndpointDstCosts> getWeightedEndPointCostMap() {
        return weightedEndpointDstCostsSortedMap;
    }

    @JsonAnySetter
    public void setWeightedEndPointCostMap(SortedMap<TypedEndpointAddr, WeightedEndpointDstCosts> endPointCostMap) {
        this.weightedEndpointDstCostsSortedMap = endPointCostMap;
    }

    public static void createOrdinalMap(WeightedEndpointCostMapData eCostMapData, EndpointCostMapData endpointCostMapData){

        WeightedEndpointCostMapData ecmd = new WeightedEndpointCostMapData();

        int i = 1;

        for(Map.Entry<TypedEndpointAddr, WeightedEndpointDstCosts> eToeW : eCostMapData.weightedEndpointDstCostsSortedMap.entrySet()) {

            SortedMap<TypedEndpointAddr, WeightedEndpointDstCosts> newetoe = ecmd.getWeightedEndPointCostMap();

            SortedMap<TypedEndpointAddr, EndpointDstCosts> eDstcosts = endpointCostMapData.getEndPointCostMap();

            newetoe.put(eToeW.getKey(), new WeightedEndpointDstCosts());

            SortedMap<TypedEndpointAddr, Double> dstcosts= eToeW.getValue().getDstCosts();

            for(Map.Entry<TypedEndpointAddr, Double> costToEnd : sortByValue(dstcosts).entrySet()) {
                eDstcosts.get(eToeW.getKey()).getDstCosts().put(costToEnd.getKey(), i);
                i++;
            }

            i = 1;
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
