/*
 * Copyright 2017-present Open Networking Laboratory
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.ollora.thesis.alto;

import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.commands.Option;
import org.onlab.packet.IpAddress;
import org.onosproject.cli.AbstractShellCommand;
import org.onosproject.net.Host;

import java.util.List;
import java.util.Map;


/**
 * Sample Apache Karaf CLI command
 */
@Command(scope = "onos", name = "alto",
         description = "Command to manage ALTO services")
public class ALTOCommand extends AbstractShellCommand {


    @Option(name = "pids", aliases = {"-p", "--pids", "pids"},
            description = "Print PIDs and host IPs",
            required = false, multiValued = false)
    boolean printPIDs = false;

    @Option(name = "cost", aliases = {"-c", "--costs", "cost"},
            description = "Print Cost from pid to pid",
            required = false, multiValued = false)
    boolean printCosts = false;

    @Option(name = "vtag", aliases = {"-v", "--vtag", "vtag"},
            description = "Print the current Vtag",
            required = false, multiValued = false)
    boolean printVtags = false;


    @Override
    protected void execute() {

        ALTOService altoService = get(ALTOService.class);

        if(printPIDs) {
            Map<PID, List<Host>> pids = altoService.getPIDs();
            printPIDs(pids);
        }

        if(printCosts){
            Map<String, DstCosts> costs = altoService.getCostData();
            printCosts(costs);
        }

        if(printVtags){
            List<VersionTag> vtags = altoService.getAllVersionTags();
            printVtags(vtags);
        }

    }

    private void printPIDs(Map<PID, List<Host>> pids){

        printBar();

        for(Map.Entry<PID, List<Host>> entry : pids.entrySet()){

            List<Host> hostList = entry.getValue();

            print(entry.getKey().getPidName()+": ");

            for(Host host: hostList){
                for(IpAddress ip : host.ipAddresses()){
                    print("    -> "+ip.toString());
                }
            }
        }
    }

    private void printVtags(List<VersionTag> vtags){

        printBar();
        print("Latest "+vtags.size()+" version tags:");

        int i = 0;

        for(VersionTag tag : vtags){
            i++;

            if(i == vtags.size())
                print(tag.getTag()+"   <-- Current");
            else
                print(tag.getTag());
        }

    }

    private void printCosts(Map<String, DstCosts> costs){

        printBar();

        for(Map.Entry<String, DstCosts> cost : costs.entrySet()){

            print("Source PID: "+cost.getKey());

            for(Map.Entry<String, Integer> destcosts : cost.getValue().getDstCosts().entrySet()){
                print("  -> Destination PID: "+destcosts.getKey()+" - Cost: "+destcosts.getValue().toString());
            }
        }
    }

    private void printBar(){

        print("\n- - - - - - - - - - - - - - - - - -\n");

    }

}
