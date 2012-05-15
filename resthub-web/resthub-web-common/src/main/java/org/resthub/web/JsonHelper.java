package org.resthub.web;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import org.resthub.web.exception.SerializationException;

/**
 * Helper for JSON serialization and deserialization
 */
public class JsonHelper {

    protected static ObjectMapper getJsonObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        AnnotationIntrospector introspector = new JacksonAnnotationIntrospector();
        mapper.setAnnotationIntrospector(introspector);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);       

        return mapper;
    }

    /**
     * Serialize and object to a JSON String representation
     * @param o The object to serialize
     * @return The JSON String representation
     */
    public static String serialize(Object o) {
        ObjectMapper mapper = getJsonObjectMapper();
        OutputStream baOutputStream = new ByteArrayOutputStream();
        try {
            mapper.writeValue(baOutputStream, o);
        } catch (Exception e) {
            throw new SerializationException(e);
        }
        return baOutputStream.toString();
    }

    /**
     * Deserialize a JSON string
     * @param content The JSON String object representation
     * @param type The type of the deserialized object instance
     * @return The deserialized object instance
     */
    public static <T> T deserialize(String content, Class<T> type) {
        ObjectMapper mapper = getJsonObjectMapper();
        try {
            return type.cast(mapper.readValue(content, type));
        } catch (Exception e) {
            throw new SerializationException(e);
        }
    }
}
