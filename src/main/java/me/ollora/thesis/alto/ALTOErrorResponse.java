package me.ollora.thesis.alto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;

/**
 * Created by s150924 on 3/29/17.
 */



public class ALTOErrorResponse implements Serializable {

    @JsonProperty("meta")
    private ErrorMeta eMeta;

    public ALTOErrorResponse(){
        this.eMeta = new ErrorMeta();
    }

    public ErrorMeta geteMeta() {
        return eMeta;
    }

    public void seteMeta(ErrorMeta eMeta) {
        this.eMeta = eMeta;
    }

    public String toJSON(){

        ObjectMapper mapper = new ObjectMapper();
        String json = null;

        try {
            json = mapper.writeValueAsString(this);
        } catch (JsonProcessingException exc) {
            exc.printStackTrace();
        }

        return json;
    }

    public class ErrorMeta {

        @JsonProperty("code")
        private String code;

        @JsonProperty("syntax-error")
        private String syntaxError;

        @JsonProperty("field")
        private String field;

        @JsonProperty("value") //Specifying this property requires field property to be specified too
        private String value;

        public ErrorMeta(){
            this.code = null;
            this.syntaxError = null;
            this.field = null;
            this.value = null;
        }

        @JsonProperty("code")
        public String getCode() {
            return code;
        }

        @JsonProperty("code")
        public void setCode(String code) {
            this.code = code;
        }

        @JsonProperty("syntax-error")
        public String getSyntaxError() {
            return syntaxError;
        }

        @JsonProperty("syntax-error")
        public void setSyntaxError(String syntaxError) {
            this.syntaxError = syntaxError;
        }

        @JsonProperty("field")
        public String getField() {
            return field;
        }

        @JsonProperty("field")
        public void setField(String field) {
            this.field = field;
        }

        @JsonProperty("value")
        public String getValue() {
            return value;
        }

        @JsonProperty("value")
        public void setValue(String value) {
            this.value = value;
        }

    }
}
