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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.onosproject.rest.AbstractWebResource;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.io.IOException;

/**
 * ALTO REST interface.
 */

@Path("alto")
public class AppWebResource extends AbstractWebResource {

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected ALTOService altoService;


    @GET
    @Path("directory")
    @Produces({ALTOMediaType.APPLICATION_ALTO_NETWORKMAP,
            ALTOMediaType.APPLICATION_ALTO_ERROR})
    public Response returnDirectory() {

        altoService = get(ALTOService.class);

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
    @Path("networkmap")
    @Produces({ALTOMediaType.APPLICATION_ALTO_NETWORKMAP,
            ALTOMediaType.APPLICATION_ALTO_ERROR})
    public Response returnNetworkMap() {

        altoService = get(ALTOService.class);

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
    @Path("networkmap/filtered")
    @Consumes(ALTOMediaType.APPLICATION_ALTO_NETWORKMAPFILTER)
    @Produces({ALTOMediaType.APPLICATION_ALTO_NETWORKMAP,
               ALTOMediaType.APPLICATION_ALTO_ERROR})
    public Response returnFilteredNetworkMap(String body) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);


        ReqFilteredNetworkMap filNetMap = null;
        InfoResourceNetworkMap netMap = null;

        try {
            filNetMap = mapper.readValue(body, ReqFilteredNetworkMap.class);
        }
        catch (JsonParseException | JsonMappingException syntaxEx){

            //Either json structures is incorrect or field not present in POJO

            ALTOErrorResponse errResponse = new ALTOErrorResponse();

            errResponse.geteMeta().setCode(ALTOErrorCodes.E_SYNTAX);
            errResponse.geteMeta().setSyntaxError(
                    "Col: "+syntaxEx.getLocation().getColumnNr()+". "+
                    "Line: "+syntaxEx.getLocation().getLineNr()+". "+
                    "Offset: "+syntaxEx.getLocation().getCharOffset()+"."
            );

            return ok(errResponse.toJSON())
                    .status(400)
                    .type(ALTOMediaType.APPLICATION_ALTO_ERROR)
                    .build();

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if(filNetMap.getPids() == null){

            ALTOErrorResponse errResponse = new ALTOErrorResponse();

            errResponse.geteMeta().setCode(ALTOErrorCodes.E_MISSING_FIELD);
            errResponse.geteMeta().setField("pids");

            return ok(errResponse.toJSON())
                    .status(400)
                    .type(ALTOMediaType.APPLICATION_ALTO_ERROR)
                    .build();
        }

        altoService = get(ALTOService.class);

        netMap = altoService.getNetworkMap();

        netMap.filterMap(filNetMap);

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
    @Path("costmap/{mode}/{metric}")
    @Produces({ALTOMediaType.APPLICATION_ALTO_COSTMAP,
            ALTOMediaType.APPLICATION_ALTO_ERROR})
    public Response returnCostMap(@QueryParam("mode")String mode, @QueryParam("metric")String metric) {

        altoService = get(ALTOService.class);

        if (mode.equals("num")) mode = "numerical";
        if (mode.equals("ord")) mode = "ordinalal";

        CostType costType = new CostType(metric, mode);

        InfoResourceCostMap costMap = altoService.getCostMap();

        if(!costMap.getSetOfCostMaps().containsKey(costType)){

            ALTOErrorResponse errResponse = new ALTOErrorResponse();

            errResponse.geteMeta().setCode(ALTOErrorCodes.E_INVALID_FIELD_VALUE);
            errResponse.geteMeta().setField(mode+"/"+metric);

            Boolean foundMetric = false;

            for(CostType ct : costMap.getSetOfCostMaps().keySet()){
                if(ct.getCostMetric().equals(metric))
                    foundMetric = true;
            }

            if(foundMetric == true) {
                errResponse.geteMeta().setValue(mode);
            }else{
                errResponse.geteMeta().setValue(metric);
            }

            return ok(errResponse.toJSON())
                    .status(400)
                    .type(ALTOMediaType.APPLICATION_ALTO_ERROR)
                    .build();

        }

        costMap.setCostMap(costMap.getSetOfCostMaps().get(costType));

        ObjectMapper mapper = new ObjectMapper();
        String json = null;

        try {
            json = mapper.writeValueAsString(costMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return ok(json).build();
    }

    @POST
    @Path("costmap/filtered")
    @Consumes(ALTOMediaType.APPLICATION_ALTO_COSTMAPFILTER)
    @Produces({ALTOMediaType.APPLICATION_ALTO_COSTMAP,
            ALTOMediaType.APPLICATION_ALTO_ERROR})
    public Response returnFilteredCostMap(String body) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);


        ReqFilteredCostMap filter = null;
        InfoResourceCostMap costMap = null;

        try {
            filter = mapper.readValue(body, ReqFilteredCostMap.class);
        }
        catch (JsonParseException | JsonMappingException syntaxEx){

            //Either json structure is incorrect or field not present in POJO
            //right now unknown properties do not raise an exception see: mapper.disable() ...

            ALTOErrorResponse errResponse = new ALTOErrorResponse();

            errResponse.geteMeta().setCode(ALTOErrorCodes.E_SYNTAX);
            errResponse.geteMeta().setSyntaxError(
                    "Column: "+syntaxEx.getLocation().getColumnNr()+". "+
                    "Line: "+syntaxEx.getLocation().getLineNr()+". "+
                    "Offset: "+syntaxEx.getLocation().getCharOffset()+"."
            );

            return ok(errResponse.toJSON())
                    .status(400)
                    .type(ALTOMediaType.APPLICATION_ALTO_ERROR)
                    .build();

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if(filter.getCostType() == null){

            ALTOErrorResponse errResponse = new ALTOErrorResponse();

            errResponse.geteMeta().setCode(ALTOErrorCodes.E_MISSING_FIELD);
            errResponse.geteMeta().setField("cost-type");

            return ok(errResponse.toJSON())
                    .status(400)
                    .type(ALTOMediaType.APPLICATION_ALTO_ERROR)
                    .build();
        }

        altoService = get(ALTOService.class);

        costMap = altoService.getCostMap();

        costMap.filterMap(filter);

        String json = null;

        try {
            json = mapper.writeValueAsString(costMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return ok(json)
                .type(ALTOMediaType.APPLICATION_ALTO_NETWORKMAP)
                .build();
    }



}
