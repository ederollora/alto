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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.onosproject.rest.AbstractWebResource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static org.onlab.util.Tools.nullIsNotFound;

/**
 * Sample web resource.
 */
@Path("alto")
public class AppWebResource extends AbstractWebResource {

    @GET
    @Path("networkmap")
    @Produces(AltoMediaType.APPLICATION_ALTO_NETWORKMAP)
    public Response returnNetworkMap() {

        ALTOService altoService = get(ALTOService.class);

        InfoResourceNetworkMap netMap = altoService.getNetworkMap();

        ObjectMapper mapper = new ObjectMapper();
        String json = null;

        try {
            json = mapper.writeValueAsString(netMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return ok(json).build();

    }

    @GET
    @Path("costmap")
    @Produces(AltoMediaType.APPLICATION_ALTO_COSTMAP)
    public Response returnCostMap() {

        ALTOService altoService = get(ALTOService.class);

        InfoResourceCostMap costMap = altoService.getCostMap();

        ObjectMapper mapper = new ObjectMapper();
        String json = null;

        try {
            json = mapper.writeValueAsString(costMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return ok(json).build();

    }

}
