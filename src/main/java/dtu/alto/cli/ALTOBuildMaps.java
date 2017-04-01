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

@Command(scope = "onos", name = "alto-rbmaps",
        description = "Current Costmap")
public class ALTOBuildMaps extends AbstractShellCommand {

    @Override
    protected void execute() {

        ALTOService altoService = get(ALTOService.class);

        altoService.rebuildMaps();

        printBar();

        log.info("Rebuilding maps...");

    }


    private void printBar(){

        print("\n- - - - - - - - - - - - - - - - - -\n");

    }


}
