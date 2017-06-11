package dtu.alto.net;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import dtu.alto.base.EndpointAddrGroup;
import dtu.alto.endpoint.AddressType;
import dtu.alto.endpoint.EndpointAddr;
import dtu.alto.pid.PIDName;
import org.onosproject.net.Host;
import org.onosproject.net.HostId;
import org.onosproject.net.device.DeviceService;
import org.onosproject.net.host.HostAdminService;
import org.slf4j.Logger;

import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.*;

/**
 * Created by Eder Ollora Zaballa: s150924@student.dtu.dk on 3/10/17.
 */
public class NetworkMapData implements Serializable {


    private Map<PIDName, EndpointAddrGroup> data = null;

    @JsonIgnore
    private Map<String, PIDName> pids = null;

    @JsonIgnore
    private Map<PIDName, List<Host>> pidList = null;

    private int createdPIDs = 0;


    public NetworkMapData(){

        data = new LinkedHashMap<>();
        pids = new LinkedHashMap<>();
        pidList = new LinkedHashMap<>();
    }

    public NetworkMapData(Iterable<Host> hosts, Logger log,
                          DeviceService deviceService, HostAdminService hostAdminService){

        this();

        for (Host host: hosts) {
            try {
                this.insertHost(host, log, deviceService, hostAdminService);
            } catch (UnknownHostException e) {
                //String ip = host.ipAddresses().iterator().next().getIp4Address().toString();
                log.error(e.getMessage() + ": IP from host [" + host.id() + "] with MAC [" + host.mac() + "] is unknown.");
            }

        }

        setOutsidePID();

    }

    //copy method
    public NetworkMapData(NetworkMapData nData){
        this.data = new LinkedHashMap<>(nData.getData());
        this.pids = new LinkedHashMap<>(nData.getPids());
        this.pidList = new LinkedHashMap<>(nData.getPidList());
    }

    @JsonAnyGetter
    public Map<PIDName, EndpointAddrGroup> getData() {
        return data;
    }

    @JsonAnySetter
    public void setData(Map<PIDName, EndpointAddrGroup> data) {
        this.data = data;
    }

    @JsonIgnore
    public Map<String, PIDName> getPids() {
        return pids;
    }

    @JsonIgnore
    public void setPids(Map<String, PIDName> pids) {
        this.pids = pids;
    }

    @JsonIgnore
    public Map<PIDName, List<Host>> getPidList() {
        return pidList;
    }

    @JsonIgnore
    public void setPidList(Map<PIDName, List<Host>> pidList) {
        this.pidList = pidList;
    }

    public void insertHost(Host host, Logger log, DeviceService deviceService,
                           HostAdminService hostAdminService) throws UnknownHostException {

        Boolean found = false;

        String attachDevice = host.location().deviceId().toString();

        for (Map.Entry<PIDName, EndpointAddrGroup> entry : data.entrySet()) {

            PIDName pid = entry.getKey();
            EndpointAddrGroup aGr = entry.getValue();

            if(pid != null){ // if we reach here it should be != null

                if(pid.getRefDevice().equals(attachDevice)){

                    if(host.ipAddresses().size() > 0) {

                        String ipAddress = host.ipAddresses().iterator().next().getIp4Address().toString();
                        //String ipAddressAndMask = host.ipAddresses().iterator().next().toIpPrefix().toString();
                        //log.info("IP ["+ipAddress+"] / Mask ["+mask+"}");

                        aGr.insertHost(ipAddress);

                        pidList.get(pid).add(host);

                        found = true;
                    }else{
                        hostAdminService.removeHost(host.id());
                        log.info("Host with id ["+host.id()+"]. Removing the \"false\" host from ONOS");
                        break;
                    }

                    // If host has either known or unknown IP break anyway
                    // since we found same reference device (attach point)
                }
            }

        }

        if(!found){

            if(host.ipAddresses().size() > 0) {

                PIDName pid = new PIDName(++createdPIDs,
                                   host.location().deviceId().toString(),
                                    deviceService.getDevice(host.location().deviceId()));

                pids.put(pid.getName(), pid);

                pidList.put(pid, new ArrayList<>());
                pidList.get(pid).add(host);

                String ipAddress = host.ipAddresses().iterator().next().getIp4Address().toString();
                //String ipAddressAndMask = host.ipAddresses().iterator().next().toIpPrefix().toString();

                EndpointAddrGroup gr = new EndpointAddrGroup(ipAddress);
                data.put(pid, gr);

            }else{
                hostAdminService.removeHost(host.id());
                log.info("Host with id ["+host.id()+"]. Removing the \"false\" host from ONOS");
            }
        }
        //log.info("Created PIDs: "+String.valueOf(createdPIDs));
    }

    public void setOutsidePID(){

        PIDName pid = new PIDName(++createdPIDs);
        pids.put(pid.getName(), pid);

        data.put(pid, new EndpointAddrGroup());

        data.get(pid).getEndGr().put(
                AddressType.IPV4,
                new ArrayList<>(Arrays.asList(new EndpointAddr("0.0.0.0/0")))
        );

        data.get(pid).getEndGr().put(
                AddressType.IPV6,
                new ArrayList<>(Arrays.asList(new EndpointAddr("::/0")))
        );

    }
}
