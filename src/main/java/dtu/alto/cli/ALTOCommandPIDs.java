package dtu.alto.cli;

import dtu.alto.core.ALTOService;
import dtu.alto.pid.PID;
import org.apache.karaf.shell.commands.Command;
import org.onlab.packet.IpAddress;
import org.onosproject.cli.AbstractShellCommand;
import org.onosproject.net.Host;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by s150924 on 3/31/17.
 */

@Command(scope = "onos", name = "alto-pids",
        description = "List endpoints per PIDs")
public class ALTOCommandPIDs extends AbstractShellCommand {

    @Override
    protected void execute() {

        ALTOService altoService = get(ALTOService.class);

        Map<PID, List<Host>> pids = altoService.getPIDs();
        printPIDs(pids);

    }

    private void printPIDs(Map<PID, List<Host>> pids){

        printBar();

        SortedMap<PID, List<Host>> sPIDs = new TreeMap<>();

        sPIDs.putAll(pids);

        for(SortedMap.Entry<PID, List<Host>> entry : sPIDs.entrySet()){

            List<Host> hostList = entry.getValue();

            print(entry.getKey().getPidName()+": ");

            for(Host host: hostList){
                for(IpAddress ip : host.ipAddresses()){
                    print("    -> "+ip.toString());
                }
            }
        }
    }

    private void printBar(){
        print("\n- - - - - - - - - - - - - - - - - -\n");

    }


}
