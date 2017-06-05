package dtu.alto.cdn;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by s150924 on 6/5/17.
 */
public class AckMessage implements Serializable {

    @JsonProperty("message")
    String message = null;

    public AckMessage() {
        this.message = "OK";
    }

    public AckMessage(String message) {
        this();
        this.message = message;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }
}
