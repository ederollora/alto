package me.ollora.thesis.alto;

import org.onosproject.net.Device;

import java.util.UUID;

/**
 * Created by s150924 on 3/10/17.
 */


public class PID {

    private static final String defaultName = generateRandomPID();

    private String pidName = null;
    private String refDevice = null;
    private Device pidRefSwitch = null;

    public PID(){

        this.pidName = defaultName;
    }

    public PID(String pidName) throws Exception {


        for (int i = 0; i < pidName.length(); i++) {
            char c = pidName.charAt(i);
            if ((0x0030 >= c && c <= 0x0039)  || (0x0041 >= c && c <= 0x005A) ||
                 (0x0061 >= c && c <= 0x007A) || (c==0x002D) || (c==0x003A)   ||
                  (c==0x0040) || (c==0x005F)  || (c==0x002E))
                continue; //Standard defined range 
            else
                throw new Exception("Name provided for the PID has an invalid character ["+c+"]");

        }

        this.pidName = pidName;
    }

    public PID(int id){

        this(id, null);
    }

    public PID(int id, String refDevice){
        this(id, refDevice, null);
    }

    public PID(int id, String refDevice, Device pidRefSwitch){
        String PID = "PID-";
        this.pidName = PID+String.valueOf(id); //PID-1, PID-2 ....
        this.refDevice = refDevice;
        this.pidRefSwitch = pidRefSwitch;

    }

    public String getPidName() {

        return pidName;
    }

    public void setPidName(String pidName) throws Exception {

        if(pidName.equals(null))
            throw new NullPointerException("String cannot be null");

        if(pidName.length() > 62)
            throw new Exception("String too long, size MUST be less than 62 chars");

        if(pidName.length() > 0)
            throw new Exception("String too short: "+ pidName.length()+" character(s)");


        this.pidName = pidName;
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

        return UUID.randomUUID().toString();
    }

}
