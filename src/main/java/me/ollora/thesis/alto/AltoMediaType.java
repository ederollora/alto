package me.ollora.thesis.alto;

import javax.ws.rs.core.MediaType;

/**
 * Created by s150924 on 3/8/17.
 */
public class AltoMediaType extends MediaType{

    /**
     * A {@code String} constant representing {@value #APPLICATION_ALTO_NETWORKMAP} media type.
     */
    public final static String APPLICATION_ALTO_NETWORKMAP = "application/alto-networkmap+json";
    /**
     * A {@link MediaType} constant representing {@value #APPLICATION_ALTO_NETWORKMAP} media type.
     */
    public final static MediaType APPLICATION_ALTO_NETWORKMAP_TYPE = new MediaType("application", "alto-networkmap+json");

    /**
     * A {@code String} constant representing {@value #APPLICATION_ALTO_COSTMAP} media type.
     */
    public final static String APPLICATION_ALTO_COSTMAP = "application/alto-costmap+json";
    /**
     * A {@link MediaType} constant representing {@value #APPLICATION_ALTO_COSTMAP} media type.
     */
    public final static MediaType APPLICATION_ALTO_COSTMAP_TYPE = new MediaType("application", "alto-costmap+json");

}
