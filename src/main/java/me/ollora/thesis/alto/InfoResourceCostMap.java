package me.ollora.thesis.alto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.*;

/**
 * Created by s150924 on 3/15/17.
 */


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "meta",
        "cost-map"
})


public class InfoResourceCostMap extends ResponseEntityBase{

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

    @JsonIgnore


    public InfoResourceCostMap(){

        this.costMap = new CostMapData();
    }

    public InfoResourceCostMap(VersionTag vTag) {
        this.getMeta().setDependentVersionTags(new ArrayList<>());
        this.getMeta().getDependentVersionTags().add(vTag);
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

        if(filCostMap.getCostType() != null)
            this.setCostMap(this.getSetOfCostMaps().get(filCostMap.getCostType()));

        if(filCostMap.getPids() != null){

            PIDFilter filter = filCostMap.getPids();

            //remove unknown source & destination PIDs
            // Should use a PID List instead of costmap keys...
            filter.getSrcs().retainAll(this.getCostMap().getData().keySet());
            filter.getDsts().retainAll(this.getCostMap().getData().keySet());


            if(filter.getSrcs() != null && filter.getSrcs().size() > 0)
                this.getCostMap().getData().keySet().retainAll(filter.getSrcs());



            if(filter.getDsts() != null && filter.getDsts().size() > 0){
                for(Map.Entry<String, DstCosts> pair : this.getCostMap().getData().entrySet())
                    pair.getValue().getDstCosts().keySet().retainAll(filter.getDsts());
            }
        }

        if(filCostMap.getConstraints() != null
                && filCostMap.getConstraints().size() > 0){

            for(Map.Entry<String, DstCosts> pair : this.getCostMap().getData().entrySet()) {

                Iterator<Map.Entry<String, Integer>> it = pair.getValue().getDstCosts().entrySet().iterator();

                while (it.hasNext()) {

                    Map.Entry<String, Integer> entry = it.next();

                    if (!fulfillsConstraints(filCostMap.getConstraints(), entry.getValue()))
                        it.remove();
                }
            }
        }
    }

    private boolean fulfillsConstraints(List<String> constraints, int value){

        String operator, target;
        Float fOperator;

        for(String constraint : constraints){

            String[] entities = constraint.trim().split("\\s+");

            if(entities.length != 2)
                return false; //maybe continue;?

            operator = entities[OPERATOR_POSITION].trim();
            target = entities[TARGET_POSITION].trim();

            if(!supportedOperators.contains(operator))
                return false; //maybe continue;?

            try{
                fOperator = Float.parseFloat(target);
            }catch (NumberFormatException e){
                //instead of try catch, you can use a regexp
                return false; //maybe continue;?
            }

            if(operator.equals(GREATER_THAN))
                if(!(value > fOperator)) return false;
            if(operator.equals(LOWER_THAN))
                if(!(value < fOperator)) return false;
            if(operator.equals(GREATER_OR_EQUAL))
                if(!(value >= fOperator)) return false;
            if(operator.equals(LOWER_OR_EQUAL))
                if(!(value <= fOperator)) return false;
            if(operator.equals(EQUAL))
                if(!(value == fOperator)) return false;

        }

        return true;
    }

}
