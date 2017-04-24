package dtu.alto.endpoint;


import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

/**
 * Created by s150924 on 4/23/17.
 */
public class EndpointAddr implements Serializable {


    String address = null;

    public EndpointAddr() {}

    public EndpointAddr(String addr) {
        this.address = addr;
    }

    @JsonValue
    public String getAddress() {
        return address;
    }

    @JsonValue
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EndpointAddr that = (EndpointAddr) o;

        return address.equals(that.address);
    }

    @Override
    public int hashCode() {
        return address.hashCode();
    }

}
