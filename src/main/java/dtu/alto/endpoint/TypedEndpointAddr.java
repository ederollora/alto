package dtu.alto.endpoint;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * Created by s150924 on 4/22/17.
 */



public class TypedEndpointAddr implements Serializable{

    private String typedAddress = null;

    @JsonIgnore
    private AddressType addressType = null;

    @JsonIgnore
    private EndpointAddr endpointAddr = null;

    public TypedEndpointAddr(){}

    public TypedEndpointAddr(String typedAddress){
        this.typedAddress = typedAddress;
    }

    public String getTypedAddress() {
        return typedAddress;
    }

    public void setTypedAddress(String typedAddress) {
        this.typedAddress = typedAddress;

        //if(typedAddress.indexOf(":") == -1)
            //throw new InvalidTypedAddrFormatException("The typed address is missing the colon punctuation mark (':', U+003A)");

        //String[] splitAddr = typedAddress.split(":");


        //if(splitAddr.length < 2)
            //throw new InvalidTypedAddrFormatException("Missing type, address, or colon misplacement");

        //this.setAddressType(new AddressType(splitAddr[0]));
        //this.setEndpointAddr(new EndpointAddr(splitAddr[1]));

    }

    public AddressType getAddressType() {
        return addressType;
    }

    public void setAddressType(AddressType addressType) {
        this.addressType = addressType;
    }

    public EndpointAddr getEndpointAddr() {
        return endpointAddr;
    }

    public void setEndpointAddr(EndpointAddr endpointAddr) {
        this.endpointAddr = endpointAddr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TypedEndpointAddr that = (TypedEndpointAddr) o;

        return endpointAddr.equals(that.endpointAddr);
    }

    @Override
    public int hashCode() {
        return endpointAddr.hashCode();
    }

    @Override
    public String toString(){
        return typedAddress;
    }
}
