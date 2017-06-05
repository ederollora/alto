package dtu.alto.resources;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import dtu.alto.base.ResponseEntityBase;
import dtu.alto.base.ResponseMeta;
import dtu.alto.base.VersionTag;
import dtu.alto.cost.CostMapData;
import dtu.alto.cost.CostType;
import dtu.alto.cost.DstCosts;
import dtu.alto.endpoint.TypedEndpointAddr;
import dtu.alto.pid.PIDFilter;
import dtu.alto.pid.PIDName;
import dtu.alto.rest.ReqFilteredCostMap;

import java.util.*;

/**
 * Created by s150924 on 3/15/17.
 */


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "meta",
        "cost-map"
})


public class InfoResourceCostMap extends ResponseEntityBase {

    //Filtering parameters
    private int OPERATOR_POSITION = 0;
    private int TARGET_POSITION = 1;
    private String GREATER_THAN = "gt";
    private String LOWER_THAN = "lt";
    private String GREATER_OR_EQUAL = "ge";
    private String LOWER_OR_EQUAL = "le";
    private String EQUAL = "eq";

    private List<String> supportedOperators = Arrays.asList("gt", "lt", "ge", "le", "eq");

    @JsonProperty("cost-map")
    private CostMapData costMap = null;

    @JsonIgnore
    private Map<CostType, CostMapData> setOfCostMaps = null;


    public InfoResourceCostMap(){

        this.costMap = new CostMapData();
        this.setOfCostMaps = new HashMap<>();
    }

    public InfoResourceCostMap(VersionTag vTag) {
        this();
        this.getMeta().setDependentVersionTags(new ArrayList<>());
        this.getMeta().getDependentVersionTags().add(vTag);
    }

    public InfoResourceCostMap(ResponseMeta meta,
                               CostMapData costData,
                               Map<CostType, CostMapData> setOfData){

        super(meta);
        this.costMap = new CostMapData(costData);
        this.setOfCostMaps = new HashMap<>(setOfData);
    }

    @JsonProperty("cost-map")
    public CostMapData getCostMap() {
        return costMap;
    }

    @JsonProperty("cost-map")
    public void setCostMap(CostMapData costMap) {
        this.costMap = costMap;
    }

    @JsonIgnore
    public Map<CostType, CostMapData> getSetOfCostMaps() {
        return setOfCostMaps;
    }

    @JsonIgnore
    public void setSetOfCostMaps(Map<CostType, CostMapData> setOfCostMaps) {
        this.setOfCostMaps = setOfCostMaps;
    }

    public void filterMap(ReqFilteredCostMap filCostMap){

        // cost type filtering
        if(filCostMap.getCostType() != null)
            this.setCostMap(this.getSetOfCostMaps().get(filCostMap.getCostType()));

        // source/destination pid filtering
        if(filCostMap.getPids() != null){

            PIDFilter filter = filCostMap.getPids();

            //remove unknown source & destination PIDs
            // Should use a PIDName List instead of cost keys...
            Set<PIDName> existingPIDs = this.getCostMap().getData().keySet();

            if(filter.getSrcs() != null && filter.getSrcs().size() > 0) {
                //existingPIDs.retainAll(filter.getSrcs());
                filter.getSrcs().retainAll(existingPIDs);
            }


            if(filter.getDsts() != null && filter.getDsts().size() > 0){
                //existingPIDs.retainAll(filter.getDsts());
                filter.getDsts().retainAll(existingPIDs);
                CostMapData costMapData = new CostMapData();

                //MORDOR
                for(Map.Entry<PIDName, DstCosts> srcTodsts : this.getCostMap().getData().entrySet()){

                    if(filter.getSrcs().contains(srcTodsts.getKey()) &&
                            !costMapData.getData().containsKey(srcTodsts.getKey())) {
                        costMapData.getData().put(srcTodsts.getKey(), new DstCosts());

                        for (Map.Entry<PIDName, Integer> dst : srcTodsts.getValue().getDstCosts().entrySet()){
                            // if destination is in the filter & it has not been added yet (to avoid duplicates)

                            if(filter.getDsts().contains(dst.getKey()) &&
                                    !costMapData.getData().get(srcTodsts.getKey()).getDstCosts().containsKey(dst.getKey())) {
                                costMapData.getData().get(srcTodsts.getKey()).getDstCosts().put(dst.getKey(), dst.getValue());
                            }
                        }
                    }
                }

                this.costMap = costMapData;

                //filter.getDsts().retainAll(pair.getValue().getDstCosts().values());
                //pair.getValue().getDstCosts().keySet().retainAll(filter.getDsts());
            }
        }

        // constraint filtering
        if(filCostMap.getConstraints() != null
                && filCostMap.getConstraints().size() > 0){

            for(Map.Entry<PIDName, DstCosts> pair : this.getCostMap().getData().entrySet()) {

                Iterator<Map.Entry<PIDName, Integer>> it = pair.getValue().getDstCosts().entrySet().iterator();

                while (it.hasNext()) {

                    Map.Entry<PIDName, Integer> entry = it.next();

                    if (!fulfillsConstraints(filCostMap.getConstraints(), entry.getValue()))
                        it.remove();
                }
            }
        }
    }

    private boolean fulfillsConstraints(List<String> constraints, int value){

        String operator, target;
        double doubleCost = new Double(value).doubleValue();
        double fOperator;

        for(String constraint : constraints){

            String[] entities = constraint.trim().split("\\s+");

            if(entities.length != 2)
                return false; //maybe continue or false;?

            operator = entities[OPERATOR_POSITION].trim();
            target = entities[TARGET_POSITION].trim();

            if(!supportedOperators.contains(operator))
                return false; //maybe continue;?

            try{
                fOperator = Double.parseDouble(target);
            }catch (Exception ex){
                //instead of try catch, you can use a regexp
                return false; //maybe continue;?
            }


            int comparison = Double.compare(doubleCost,fOperator);

            if(operator.equals(GREATER_THAN) && !(comparison > 0)) return false;
            if(operator.equals(LOWER_THAN) && !(comparison < 0)) return false;
            if(operator.equals(GREATER_OR_EQUAL) && (comparison < 0)) return false;
            if(operator.equals(LOWER_OR_EQUAL) && (comparison > 0)) return false;
            if(operator.equals(EQUAL) && !(comparison == 0)) return false;

        }

        return true;
    }

    public InfoResourceCostMap newInstance(){
        // new copy, not a reference
        return new InfoResourceCostMap(
                this.getMeta(), this.getCostMap(), this.getSetOfCostMaps()
        );
    }


}
