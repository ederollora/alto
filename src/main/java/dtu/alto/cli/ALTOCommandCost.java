package dtu.alto.cli;

import dtu.alto.core.ALTOService;
import dtu.alto.cost.DstCosts;
import org.apache.karaf.shell.commands.Command;
import org.onosproject.cli.AbstractShellCommand;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by s150924 on 3/31/17.
 */

@Command(scope = "onos", name = "alto-cost",
        description = "Current Costmap")
public class ALTOCommandCost extends AbstractShellCommand {

    @Override
    protected void execute() {

        ALTOService altoService = get(ALTOService.class);

        Map<String, DstCosts> costs = altoService.getCostMap().getCostMap().getData();
        printCosts(costs);

    }

    private void printCosts(Map<String, DstCosts> costs){

        printBar();

        SortedMap<String, DstCosts> sCosts = new TreeMap<>();

        sCosts.putAll(costs);

        for(Map.Entry<String, DstCosts> cost : sCosts.entrySet()){

            print("Source: "+cost.getKey());

            for(Map.Entry<String, Integer> destcosts : cost.getValue().getDstCosts().entrySet()){
                print("  -> Dest: "+destcosts.getKey()+" - Cost: "+destcosts.getValue().toString());
            }
        }
    }

    private void printBar(){

        print("\n- - - - - - - - - - - - - - - - - -\n");

    }


}
