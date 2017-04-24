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
package dtu.alto.cli;

import dtu.alto.core.ALTOService;
import dtu.alto.base.VersionTag;
import org.apache.karaf.shell.commands.Command;
import org.onosproject.cli.AbstractShellCommand;

import java.util.List;


/**
 * Sample Apache Karaf CLI command
 */
@Command(scope = "onos", name = "alto-vtags",
         description = "5 last used VersionTags")
public class ALTOCommandVTags extends AbstractShellCommand {

    boolean printVtags = false;

    @Override
    protected void execute() {

        ALTOService altoService = get(ALTOService.class);

        List<VersionTag> vtags = altoService.getAllVersionTags();
        printVtags(vtags);

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



    private void printBar(){

        print("\n- - - - - - - - - - - - - - - - - -\n");

    }

}
