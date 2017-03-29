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
import org.onosproject.rest.AbstractWebResource;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.io.IOException;

/**
 * ALTO REST interface.
 */
@Path("alto")
public class AppWebResource extends AbstractWebResource {

    @GET
    @Path("networkmap")
    @Produces(ALTOMediaType.APPLICATION_ALTO_NETWORKMAP)
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

    //filtered Network Map
    @POST
    @Path("networkmap")
    @Produces({ALTOMediaType.APPLICATION_ALTO_NETWORKMAP,
               ALTOMediaType.APPLICATION_ALTO_ERROR})
    public Response returnFilteredNetworkMap(String body) throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        ReqFilteredNetworkMap filNetMap = null;

        InfoResourceNetworkMap netMap= null;

        try{
            filNetMap = mapper.readValue(body, ReqFilteredNetworkMap.class);
        }
        catch (Exception e) {
            e.printStackTrace();

            ALTOErrorCodes eCode = new ALTOErrorCodes();
            eCode.setCode(ALTOErrorCodes.E_SYNTAX);

            String message = null;
            //buildErrorMessage(eCode);
            return ok(message)
                    .type(ALTOMediaType.APPLICATION_ALTO_ERROR)
                    .build();
        }

        if(filNetMap.getPids() == null)
            System.out.println("haha");

        ALTOService altoService = get(ALTOService.class);

        netMap = altoService.getNetworkMap();

        netMap.filterPIDs(filNetMap);

        String json = null;

        try {
            json = mapper.writeValueAsString(netMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return ok(json)
                .type(ALTOMediaType.APPLICATION_ALTO_NETWORKMAP)
                .build();
    }

    @GET
    @Path("costmap")
    @Produces(ALTOMediaType.APPLICATION_ALTO_COSTMAP)
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

    private String buildErrorMessage(ALTOErrorCodes code, String syntaxError,
                                       String field, String value){

        ResponseMeta rm = new ResponseMeta();

        ObjectMapper mapper = new ObjectMapper();

       if(code != null) {
           rm.setCode(code.toString());
           if(syntaxError != null && !syntaxError.isEmpty())
               rm.setSyntaxError(syntaxError);
       }


       if(field != null && !field.isEmpty()) {
           rm.setField(field);
           if (value != null && !value.isEmpty())
               rm.setValue(value);
       }

        String json = null;

        try {
            json = mapper.writeValueAsString(rm);
        } catch (JsonProcessingException exc) {
            exc.printStackTrace();
        }

        return "";

    }

}
