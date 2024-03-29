package dtu.alto.base;

import com.fasterxml.jackson.annotation.*;
import dtu.alto.endpoint.AddressType;
import dtu.alto.endpoint.EndpointAddr;

import java.io.Serializable;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

/**
 * Created by s150924 on 3/10/17.
 */


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "ipv4",
        "ipv6"
})

public class EndpointAddrGroup implements Serializable {

    SortedMap<AddressType, List<EndpointAddr>> endGr;

    public EndpointAddrGroup(){
        endGr = new TreeMap<>();
    }

    public EndpointAddrGroup(String ipAddressAndMask) throws UnknownHostException {

        this();
        this.insertHost(ipAddressAndMask);
    }

    @JsonAnyGetter
    public SortedMap<AddressType, List<EndpointAddr>> getEndGr() {
        return endGr;
    }

    @JsonAnySetter
    public void setEndGr(SortedMap<AddressType, List<EndpointAddr>> endGr) {
        this.endGr = endGr;
    }

    public void insertHost(String ip) throws UnknownHostException {

        InetAddress address = InetAddress.getByName(ip);

        if (address instanceof Inet4Address) {
            if(!this.endGr.containsKey(AddressType.IPV4))
                endGr.put(AddressType.IPV4, new ArrayList<>());
            this.endGr.get(AddressType.IPV4).add(new EndpointAddr(ip));
        }else{
            if(!this.endGr.containsKey(AddressType.IPV6))
                endGr.put(AddressType.IPV6, new ArrayList<>());
            this.endGr.get(AddressType.IPV6).add(new EndpointAddr(ip));
        }

    }
}
