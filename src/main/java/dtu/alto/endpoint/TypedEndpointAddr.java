package dtu.alto.endpoint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dtu.alto.exception.InvalidTypedAddrException;

import java.io.Serializable;

/**
 * Created by s150924 on 4/22/17.
 */



public class TypedEndpointAddr implements Serializable, Comparable<TypedEndpointAddr>{

    private String typedAddress = null;

    @JsonIgnore
    private AddressType addressType = null;

    @JsonIgnore
    private EndpointAddr endpointAddr = null;

    public TypedEndpointAddr(){}

    public TypedEndpointAddr(String typedAddress) throws InvalidTypedAddrException {
        this.typedAddress = typedAddress;

        processTypedEndpointAddr();
    }

    public String getTypedAddress() {
        return typedAddress;
    }

    public void setTypedAddress(String typedAddress) {
        this.typedAddress = typedAddress;

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

    private void processTypedEndpointAddr()
            throws InvalidTypedAddrException {

        if(!typedAddress.contains(":"))
            throw new InvalidTypedAddrException("The typed address is missing the colon punctuation mark (':', U+003A)");

        String[] splitAddr = typedAddress.split(":");
        String addressType = splitAddr[0];
        String endpointAddr = splitAddr[1];

        if(addressType.length() == 0 || endpointAddr.length() == 0)
            throw new InvalidTypedAddrException("Colon misplacement or invalid AddressType or EndpointAddr length, usage: AddressType:EndpointAddr");

        if(!AddressType.isValidAddressType(addressType))
            throw new InvalidTypedAddrException("[ "+addressType+" ] has an invalid character according to the standard");

        if(!AddressType.supportsAddressType(addressType))
            throw new InvalidTypedAddrException("[ "+addressType+" ] is not a part of the suppoerted address types");

        this.addressType = new AddressType(addressType);
        this.endpointAddr = new EndpointAddr(endpointAddr);

    }

    private boolean isTypedEndpointAddrValid(){
        return true;
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


    @Override
    public int compareTo(TypedEndpointAddr o) {
        return this.typedAddress.compareTo(o.typedAddress);
    }
}
