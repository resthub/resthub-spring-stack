package org.resthub.oauth2.utils;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.jaxrs.Annotations;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * TODO http://bitbucket.org/ilabs/resthub/issue/26/resthub-client
 * Needs to be merged with JacksonProvider contained in resthub-web in another light jar.
 */
@Provider
@Consumes({MediaType.APPLICATION_JSON, "text/json"})
@Produces({MediaType.APPLICATION_JSON, "text/json"})
public class JacksonProvider extends JacksonJsonProvider {
    /**
     * Default annotation sets to use, if not explicitly defined during
     * construction: use JAXB annotations if found;
     */
    public final static Annotations[] DEFAULT_ANNOTATIONS = {
         Annotations.JAXB
    };

    /**
     * Default constructor, usually used when provider is automatically
     * configured to be used with JAX-RS implementation.
     */
    public JacksonProvider()
    {
        
        this(null, DEFAULT_ANNOTATIONS);
    }

    /**
     * @param annotationsToUse Annotation set(s) to use for configuring
     *    data binding
     */
    public JacksonProvider(Annotations... annotationsToUse)
    {
        this(null, annotationsToUse);
    }

    /**
     * Constructor to use when a custom mapper (usually components
     * like serializer/deserializer factories that have been configured)
     * is to be used.
     */
    public JacksonProvider(ObjectMapper mapper, Annotations[] annotationsToUse)
    {
        super(mapper, annotationsToUse);
    }

}