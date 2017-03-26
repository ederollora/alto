package me.ollora.thesis.alto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by s150924 on 3/8/17.
 */


public class ResponseEntityBase implements Serializable {

    @JsonProperty("meta")
    private ResponseMeta meta;


    public ResponseEntityBase(){

        this.meta = new ResponseMeta();
    }

    public ResponseEntityBase(ResponseMeta meta) {
        this.meta = meta;
    }


    public ResponseEntityBase(VersionTag vtag){


    }

    @JsonProperty("meta")
    public ResponseMeta getMeta() {
        return meta;
    }

    @JsonProperty("meta")
    public void setMeta(ResponseMeta meta) {
        this.meta = meta;
    }

    public Boolean hasEqualVersiontag(VersionTag thatTag){

        VersionTag thisVtag = this.meta.getVersionTag();

        return ((thisVtag.getResourceId().equals(thatTag.getResourceId())) &&
                (thisVtag.getTag().equals(thatTag.getTag())));
    }

    public VersionTag VersionTag(){

        return this.meta.getVersionTag();
    }

}



