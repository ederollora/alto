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
package dtu.alto.rest;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dtu.alto.base.ResponseMeta;
import dtu.alto.cdn.AckMessage;
import dtu.alto.cdn.ServerReport;
import dtu.alto.cost.CostMapData;
import dtu.alto.cost.CostType;
import dtu.alto.endpointcost.ReqEndpointCostMap;
import dtu.alto.pid.PIDName;
import dtu.alto.resources.InfoResourceCostMap;
import dtu.alto.resources.InfoResourceEndpointCostMap;
import dtu.alto.resources.InfoResourceNetworkMap;
import dtu.alto.error.ALTOErrorCode;
import dtu.alto.error.ALTOErrorResponse;
import dtu.alto.media.ALTOMediaType;
import dtu.alto.core.ALTOService;
import org.onosproject.net.Host;
import org.onosproject.rest.AbstractWebResource;
import org.slf4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * ALTO REST interface.
 */

@Path("alto")
public class AppWebResource extends AbstractWebResource {

    @GET
    @Path("networkmap")
    @Produces({ALTOMediaType.APPLICATION_ALTO_NETWORKMAP,
            ALTOMediaType.APPLICATION_ALTO_ERROR})
    public Response returnNetworkMap() {

        ALTOService altoService = get(ALTOService.class);

        InfoResourceNetworkMap netMap = altoService.getNetworkMap();

        ObjectMapper mapper = new ObjectMapper();
        String json = null;

        //mapper.configure(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED, true);
        //mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        try {
            json = mapper.writeValueAsString(netMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return ok(json).build();
    }

    @POST
    @Path("networkmap/filtered")
    @Consumes(ALTOMediaType.APPLICATION_ALTO_NETWORKMAPFILTER)
    @Produces({ALTOMediaType.APPLICATION_ALTO_NETWORKMAP,
               ALTOMediaType.APPLICATION_ALTO_ERROR})
    public Response returnFilteredNetworkMap(String body) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);


        ReqFilteredNetworkMap filNetMap = null;
        InfoResourceNetworkMap netMap;

        try {
            filNetMap = mapper.readValue(body, ReqFilteredNetworkMap.class);
        }
        catch (JsonParseException | JsonMappingException syntaxEx){

            //Either json structures is incorrect or field not present in POJO

            return JSONParseException(
                    syntaxEx.getLocation().getColumnNr(),
                    syntaxEx.getLocation().getLineNr(),
                    syntaxEx.getLocation().getCharOffset(),
                    body.charAt(Math.toIntExact(syntaxEx.getLocation().getCharOffset()))
            );

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if(filNetMap.getPids() == null){

            ALTOErrorResponse errResponse = new ALTOErrorResponse();

            errResponse.geteMeta().setCode(ALTOErrorCode.E_MISSING_FIELD);
            errResponse.geteMeta().setField("pids");

            return ok(errResponse.toJSON())
                    .status(400)
                    .type(ALTOMediaType.APPLICATION_ALTO_ERROR)
                    .build();
        }

        ALTOService altoService = get(ALTOService.class);

        //netMap = new InfoResourceNetworkMap(altoService.getNetworkMap());

        netMap = altoService.getNetworkMap().newInstance();

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
    public Response returnCostMap(@PathParam("mode")String md, @PathParam("metric")String metric) {

        final Logger log = getLogger(getClass());

        ALTOService altoService = get(ALTOService.class);

        String mode = md;

        if (md.equals("num")) mode = "numerical";
        if (md.equals("ord")) mode = "ordinal";

        CostType costType = new CostType(mode, metric);

        InfoResourceCostMap costMap = altoService.getCostMap();

        if(!costMap.getSetOfCostMaps().containsKey(costType)){

            ALTOErrorResponse errResponse = new ALTOErrorResponse();

            errResponse.geteMeta().setCode(ALTOErrorCode.E_INVALID_FIELD_VALUE);
            errResponse.geteMeta().setField(md+"/"+metric);

            Boolean foundMode = false;

            for(CostType ct : costMap.getSetOfCostMaps().keySet()){
                if(ct.getCostMode().equals(mode))
                    foundMode = true;
            }

            if(foundMode.equals(true)) {
                errResponse.geteMeta().setValue(metric);
            }else{
                errResponse.geteMeta().setValue(md);
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
        InfoResourceCostMap newCostMap = null;


        try {
            filter = mapper.readValue(body, ReqFilteredCostMap.class);
        }
        catch (JsonParseException | JsonMappingException syntaxEx){

            //Either json structure is incorrect or field not present in POJO
            //right now unknown properties do not raise an exception see: mapper.disable() ...

            return JSONParseException(
                    syntaxEx.getLocation().getColumnNr(),
                    syntaxEx.getLocation().getLineNr(),
                    syntaxEx.getLocation().getCharOffset(),
                    body.charAt(Math.toIntExact(syntaxEx.getLocation().getCharOffset()))
            );

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if(filter.getCostType() == null)
            return missingCostType();


        ALTOService altoService = get(ALTOService.class);

        costMap = altoService.getCostMap().newInstance();

        costMap.filterMap(filter);

        String json = null;

        try {
            json = mapper.writeValueAsString(costMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return ok(json)
                .type(ALTOMediaType.APPLICATION_ALTO_COSTMAP)
                .build();
    }

    @POST
    @Path("endpointcost/lookup")
    @Consumes(ALTOMediaType.APPLICATION_ALTO_ENDPOINTCOSTPARAMS)
    @Produces({ALTOMediaType.APPLICATION_ALTO_ENDPOINTCOST,
            ALTOMediaType.APPLICATION_ALTO_ERROR})
    public Response returnEndpointCostMap(String body) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        ReqEndpointCostMap reqEndpointCostMap = null;
        InfoResourceEndpointCostMap infoResCostMap;

        try {
            reqEndpointCostMap = mapper.readValue(body, ReqEndpointCostMap.class);
        } catch (JsonParseException | JsonMappingException syntaxEx) {

            //Either json structure is incorrect or field not present in POJO
            //right now unknown properties do not raise an exception see: mapper.disable() ...

            return JSONParseException(
                    syntaxEx.getLocation().getColumnNr(),
                    syntaxEx.getLocation().getLineNr(),
                    syntaxEx.getLocation().getCharOffset(),
                    body.charAt(Math.toIntExact(syntaxEx.getLocation().getCharOffset()))
            );
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if(reqEndpointCostMap.getCostType() == null)
            return missingCostType();

        ALTOService altoService = get(ALTOService.class);
        InfoResourceCostMap costMap = altoService.getCostMap();
        CostMapData cData = costMap.getSetOfCostMaps().get(reqEndpointCostMap.getCostType());
        Map<PIDName,List<Host>> pidList = altoService.getPIDs();

        infoResCostMap = new InfoResourceEndpointCostMap();
        ResponseMeta rMeta = new ResponseMeta(reqEndpointCostMap.getCostType());
        infoResCostMap.setMeta(rMeta);

        infoResCostMap.setCosts(cData, pidList, reqEndpointCostMap);

        String json = null;

        try {
            json = mapper.writeValueAsString(infoResCostMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return ok(json)
                .type(ALTOMediaType.APPLICATION_ALTO_ENDPOINTCOST)
                .build();
    }

    /* NEW PART */

    @POST
    @Path("rank")
    @Consumes(ALTOMediaType.APPLICATION_ALTO_ENDPOINTCOSTPARAMS)
    @Produces({ALTOMediaType.APPLICATION_ALTO_COSTMAP,
            ALTOMediaType.APPLICATION_ALTO_ERROR})
    public Response returnRankedEndpoints(String body) {




        return ok(body).build();
    }

    @POST
    @Path("cdn/statistics")
    @Consumes(ALTOMediaType.APPLICATION_JSON)
    @Produces({ALTOMediaType.APPLICATION_JSON,
            ALTOMediaType.APPLICATION_ALTO_ERROR})
    public Response storeServerStats(String body) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        ServerReport serverReport = null;

        try {
            serverReport = mapper.readValue(body, ServerReport.class);
        } catch (JsonParseException | JsonMappingException syntaxEx) {

            //Either json structure is incorrect or field not present in POJO
            //right now unknown properties do not raise an exception see: mapper.disable() ...

            return JSONParseException(
                    syntaxEx.getLocation().getColumnNr(),
                    syntaxEx.getLocation().getLineNr(),
                    syntaxEx.getLocation().getCharOffset(),
                    body.charAt(Math.toIntExact(syntaxEx.getLocation().getCharOffset()))
            );
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        ALTOService altoService = get(ALTOService.class);
        altoService.updateServerReport(serverReport);

        AckMessage ack = new AckMessage();
        String json = null;

        try {
            json = mapper.writeValueAsString(ack);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return Response.ok(json).build();

    }

    private Response missingCostType(){

        ALTOErrorResponse errResponse = new ALTOErrorResponse();

        errResponse.geteMeta().setCode(ALTOErrorCode.E_MISSING_FIELD);
        errResponse.geteMeta().setField("cost-type");

        return ok(errResponse.toJSON())
                .status(400)
                .type(ALTOMediaType.APPLICATION_ALTO_ERROR)
                .build();

    }

    private Response JSONParseException(int col, int line, long off, int pos){

        ALTOErrorResponse errResponse = new ALTOErrorResponse();

        errResponse.geteMeta().setCode(ALTOErrorCode.E_SYNTAX);
        errResponse.geteMeta().setSyntaxError(
                "Column: " + col + ". " +
                "Line: " + line + ". " +
                "Offset: " + off + ". " +
                "PositionCharacter: " + pos
        );

        return ok(errResponse.toJSON())
                .status(400)
                .type(ALTOMediaType.APPLICATION_ALTO_ERROR)
                .build();

    }

    private Response customError(String message){

        ALTOErrorResponse errResponse = new ALTOErrorResponse();

        errResponse.geteMeta().setCode(ALTOErrorCode.E_SYNTAX);
        errResponse.geteMeta().setSyntaxError(
                "Reason: "+message
        );

        return ok(errResponse.toJSON())
                .status(400)
                .type(ALTOMediaType.APPLICATION_ALTO_ERROR)
                .build();

    }

}
