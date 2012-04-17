package org.resthub.web;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.resthub.web.exception.SerializationException;

public class JsonHelper {

    protected static ObjectMapper getJsonObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        AnnotationIntrospector introspector = new JacksonAnnotationIntrospector();

        SerializationConfig sc = mapper.getSerializationConfig()
                .without(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS)
                .with((SerializationConfig.Feature.INDENT_OUTPUT))
                .withAnnotationIntrospector(introspector);
        mapper.setSerializationConfig(sc);

        DeserializationConfig dc = mapper.getDeserializationConfig()
                .without(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES)
                .withAnnotationIntrospector(introspector);
        mapper.setDeserializationConfig(dc);

        return mapper;
    }

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

    public static <T> T deserialize(String content, Class<T> type) {
        ObjectMapper mapper = getJsonObjectMapper();
        try {
            return type.cast(mapper.readValue(content, type));
        } catch (Exception e) {
            throw new SerializationException(e);
        }
    }
}
