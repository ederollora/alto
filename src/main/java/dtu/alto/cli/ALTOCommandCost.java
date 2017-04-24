package dtu.alto.cli;

import dtu.alto.core.ALTOService;
import dtu.alto.cost.DstCosts;
import dtu.alto.pid.PIDName;
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

        Map<PIDName, DstCosts> costs = altoService.getCostMap().getCostMap().getData();
        printCosts(costs);

    }

    private void printCosts(Map<PIDName, DstCosts> costs){

        printBar();

        SortedMap<PIDName, DstCosts> sCosts = new TreeMap<>();

        sCosts.putAll(costs);

        for(Map.Entry<PIDName, DstCosts> cost : sCosts.entrySet()){

            print("Source: "+cost.getKey().getName());

            for(Map.Entry<PIDName, Integer> destcosts : cost.getValue().getDstCosts().entrySet()){
                print("  -> Dest: "+destcosts.getKey().getName()+" - Cost: "+destcosts.getValue().toString());
            }
        }
    }

    private void printBar(){

        print("\n- - - - - - - - - - - - - - - - - -\n");

    }


}
