package dtu.alto.pid;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.onosproject.net.Device;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by s150924 on 3/10/17.
 */


public class PIDName implements Serializable, Comparable<PIDName>{

    private static final String defaultName = generateRandomPID();

    private String name = null;

    @JsonIgnore
    private String refDevice = null;

    @JsonIgnore
    private Device pidRefSwitch = null;

    public PIDName(){
        this.name = defaultName;
    }

    public PIDName(String name) throws Exception {

        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if ((0x0030 >= c && c <= 0x0039)  || (0x0041 >= c && c <= 0x005A) ||
                 (0x0061 >= c && c <= 0x007A) || (c==0x002D) || (c==0x003A)   ||
                  (c==0x0040) || (c==0x005F)  || (c==0x002E))
                continue; //Standard defined range 
            else
                throw new Exception("Name provided for the PIDName has an invalid character ["+c+"]");

        }

        this.name = name;
    }

    public PIDName(int id){

        this(id, null);
    }

    public PIDName(int id, String refDevice){
        this(id, refDevice, null);
    }

    public PIDName(int id, String refDevice, Device pidRefSwitch){
        String name = "PID";
        this.name = name+String.valueOf(id); //PID1, PID2 ....
        this.refDevice = refDevice;
        this.pidRefSwitch = pidRefSwitch;

    }

    public String getName() {

        return name;
    }

    public void setName(String name) throws Exception {

        if(name.equals(null))
            throw new NullPointerException("String cannot be null");

        if(name.length() > 62)
            throw new Exception("String too long, size MUST be less than 62 chars");

        if(name.length() > 0)
            throw new Exception("String too short: "+ name.length()+" character(s)");


        this.name = name;
    }

    public String getRefDevice() {
        return refDevice;
    }

    public void setRefDevice(String refDevice) {
        this.refDevice = refDevice;
    }

    public Device getPidRefSwitch() {
        return pidRefSwitch;
    }

    public void setPidRefSwitch(Device pidRefSwitch) {
        this.pidRefSwitch = pidRefSwitch;
    }

    public static String getDefaultName() {
        return defaultName;
    }

    private static String generateRandomPID(){

        UUID uuid = UUID.randomUUID();

        return uuid.toString();
    }

    @Override
    public int compareTo(PIDName o){

        return this.name.compareTo(o.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PIDName pidName = (PIDName) o;

        return name.equals(pidName.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }

}
