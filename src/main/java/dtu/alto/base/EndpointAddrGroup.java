package dtu.alto.base;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by s150924 on 3/10/17.
 */


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "ipv4",
        "ipv6"
})

public class EndpointAddrGroup implements Serializable {

    public static String IPV4 = "ipv4";
    public static String IPV6 = "ipv6";

    Map<String, ArrayList<String>> endGr = null;


    public EndpointAddrGroup(){
        endGr = new LinkedHashMap<>();
    }

    public EndpointAddrGroup(String ipAddressAndMask) throws UnknownHostException {

        this();
        this.insertHost(ipAddressAndMask);
    }


    @JsonAnyGetter
    public Map<String, ArrayList<String>> getEndGr() {
        return endGr;
    }

    @JsonAnySetter
    public void setEndGr(Map<String, ArrayList<String>> endGr) {
        this.endGr = endGr;
    }

    public void insertHost(String ipAddressAndMask) throws UnknownHostException {

        InetAddress address = InetAddress.getByName(ipAddressAndMask.split("/")[0]);

        if (address instanceof Inet4Address) {
            if(!this.endGr.containsKey(IPV4))
                endGr.put(IPV4, new ArrayList<>());
            this.endGr.get(IPV4).add(ipAddressAndMask);
        }else{
            if(!this.endGr.containsKey(IPV6))
                endGr.put(IPV6, new ArrayList<>());
            this.endGr.get(IPV6).add(ipAddressAndMask);
        }

    }
}
