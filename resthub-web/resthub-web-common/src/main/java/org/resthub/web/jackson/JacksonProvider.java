package org.resthub.web.jackson;

import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.jaxrs.Annotations;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

/**
 * JAX-RS extension intended to handle JSON serialization and deserialization.
 * We use this one provided by Jackson instead of the one provided by Jersey
 * because it realy more flexible and match our need.
 * 
 * One key point is that, unlike Jersey default feature, it does not try to use a
 * JAXB XML oriented model o serialize objects. This one is real object to JSON implementation.
 * 
 * Jackson annotations only are used, combinaison with JAXB annotations introspector seem to be buggy
 * 
 * @author sdeleuze
 */
@Provider
@Named("jacksonProvider")
@Consumes({MediaType.APPLICATION_JSON, "text/json"})
@Produces({MediaType.APPLICATION_JSON, "text/json"})
public class JacksonProvider extends JacksonJsonProvider {
    /**
     * Default annotation sets to use, if not explicitly defined during
     * construction: use JAXB annotations if found;
     */
    public final static Annotations[] DEFAULT_ANNOTATIONS = {
         Annotations.JACKSON
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

        
        _mapperConfig.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
       
        _mapperConfig.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        _mapperConfig.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
        
        // Does not work for now ...
    	//this._mapperConfig.getConfiguredMapper().setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(Visibility.ANY));
        
    }

}