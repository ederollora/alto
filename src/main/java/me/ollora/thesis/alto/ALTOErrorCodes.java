package me.ollora.thesis.alto;

/**
 * Created by s150924 on 3/28/17.
 */
public class ALTOErrorCodes {

    public static final String E_SYNTAX = "E_SYNTAX";
    public static final String E_MISSING_FIELD = "E_MISSING_FIELD";
    public static final String E_INVALID_FIELD_TYPE = "E_INVALID_FIELD_TYPE";
    public static final String E_INVALID_FIELD_VALUE = "E_INVALID_FIELD_VALUE";


    String code = null;

    public ALTOErrorCodes() {
        this.code = null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
