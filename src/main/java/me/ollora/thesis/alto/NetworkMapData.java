package me.ollora.thesis.alto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.onosproject.net.Host;
import org.onosproject.net.device.DeviceService;
import org.slf4j.Logger;

import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.*;

/**
 * Created by Eder Ollora Zaballa: s150924@student.dtu.dk on 3/10/17.
 */
public class NetworkMapData implements Serializable {


    private Map<String, EndpointAddrGroup> data = null;

    private Map<String, PID> pids = null;

    private Map<PID, List<Host>> pidList = null;

    private int createdPIDs = 0;


    public NetworkMapData(){

        data = new LinkedHashMap<>();
        pids = new HashMap<>();
        pidList = new HashMap<>();
    }

    public NetworkMapData(Iterable<Host> hosts,
                          Logger log,
                          DeviceService deviceService){

        this();

        for (Host host: hosts) {
            try {
                this.insertHost(host, log, deviceService);
            } catch (UnknownHostException e) {
                //String ip = host.ipAddresses().iterator().next().getIp4Address().toString();
                log.error(e.getMessage() + ": IP from host [" + host.id() + "] with MAC [" + host.mac() + "] is unknown.");
            }

        }

        setOutsidePID();

    }

    @JsonAnyGetter
    public Map<String, EndpointAddrGroup> getData() {
        return data;
    }

    @JsonAnySetter
    public void setData(Map<String, EndpointAddrGroup> data) {
        this.data = data;
    }

    @JsonIgnore
    public Map<String, PID> getPids() {
        return pids;
    }

    @JsonIgnore
    public void setPids(Map<String, PID> pids) {
        this.pids = pids;
    }

    @JsonIgnore
    public Map<PID, List<Host>> getPidList() {
        return pidList;
    }

    @JsonIgnore
    public void setPidList(Map<PID, List<Host>> pidList) {
        this.pidList = pidList;
    }

    public void insertHost(Host host,
                            Logger log,
                             DeviceService deviceService) throws UnknownHostException {

        Boolean found = false;

        String attachDevice = host.location().deviceId().toString();

        for (Map.Entry<String, EndpointAddrGroup> entry : data.entrySet()) {

            String pid = entry.getKey();
            EndpointAddrGroup aGr = entry.getValue();

            if(pid != null){ // if we reach here it should be != null

                if(pids.get(pid).getRefDevice().equals(attachDevice)){

                    if(host.ipAddresses().iterator().hasNext()) {

                        //String ipAddress = host.ipAddresses().iterator().next().getIp4Address().toString();
                        String ipAddressAndMask = host.ipAddresses().iterator().next().toIpPrefix().toString();
                        //log.info("IP ["+ipAddress+"] / Mask ["+mask+"}");

                        aGr.insertHost(ipAddressAndMask);

                        pidList.get(pids.get(pid)).add(host);

                        found = true;
                    }
                    break;
                    // If host has either known or unknown IP break anyway
                    // since we found same reference device (attach point)
                }
            }

        }

        if(!found){

            if(host.ipAddresses().iterator().hasNext()) {

                PID pid = new PID(++createdPIDs,
                                   host.location().deviceId().toString(),
                                    deviceService.getDevice(host.location().deviceId()));

                pids.put(pid.getPidName(), pid);

                pidList.put(pid, new ArrayList<>());
                pidList.get(pid).add(host);

                String ipAddressAndMask = host.ipAddresses().iterator().next().toIpPrefix().toString();

                EndpointAddrGroup gr = new EndpointAddrGroup(ipAddressAndMask);
                data.put(pid.getPidName(), gr);

            }else{
                log.info("Host with id ["+host.id()+"] and mac ["+host.mac()+"] has unknown IP");
            }
        }
        //log.info("Created PIDs: "+String.valueOf(createdPIDs));
    }

    public void setOutsidePID(){

        PID pid = new PID(++createdPIDs, null, null);
        pids.put(pid.getPidName(), pid);

        data.put(pid.getPidName(), new EndpointAddrGroup());

        data.get(pid.getPidName()).getEndGr().put(EndpointAddrGroup.IPV4, new ArrayList<>(Arrays.asList("0.0.0.0/0")));
        data.get(pid.getPidName()).getEndGr().put(EndpointAddrGroup.IPV6, new ArrayList<>(Arrays.asList("::/0")));
    }


}
