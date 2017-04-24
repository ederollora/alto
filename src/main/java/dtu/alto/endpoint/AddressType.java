package dtu.alto.endpoint;

import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

/**
 * Created by s150924 on 4/23/17.
 */
public class AddressType implements Serializable, Comparable<AddressType> {

    String aType = null;


    public AddressType() {}

    public AddressType(String aType) {
        this.aType = aType;
    }

    @JsonValue
    public String getaType() {
        return aType;
    }

    public void setaType(String aType) {
        this.aType = aType;
    }


    public Boolean typeIsValid(String type){

        if(type == null ||
                type.length() == 0 ||
                type.length() > 64)
            return false;

        for (int i = 0; i < type.length(); i++) {
            char c = type.charAt(i);
            if (c < 0x0030 ||
                 (c > 0x0039 && c < 0x0041) ||
                    (c > 0x005A && c < 0x0061) ||
                      c > 0x007A)
                return false;
        }
        // If we reach here the type should be valid according to the RFC
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AddressType that = (AddressType) o;

        return aType.equals(that.aType);
    }

    @Override
    public int hashCode() {
        return aType.hashCode();
    }

    @Override
    public String toString() {
        return aType;
    }

    @Override
    public int compareTo(AddressType aT) {
        return this.aType.compareTo(aT.aType);
    }
}
