package org.resthub.web;

import com.fasterxml.jackson.core.type.TypeReference;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.resthub.web.exception.SerializationException;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * Helper for XML serialization and deserialization.
 */
public class XmlHelper {
    
    /**
     * Jackson Object Mapper used to serialization/deserialization
     */
    protected static ObjectMapper objectMapper;
    
    protected static void initialize() {
        objectMapper = new XmlMapper();
        AnnotationIntrospector introspector = new JacksonAnnotationIntrospector();
        objectMapper.setAnnotationIntrospector(introspector);
    }
    
    /**
     * Return the objectMapper. It can be used to customize serialization/deserialization configuration.
     * @return 
     */
    public ObjectMapper getObjectMapper() {
        if(objectMapper == null) initialize();
        return objectMapper;
    }

    /**
     * Serialize and object to an XML String representation
     * @param o The object to serialize
     * @return The XML String representation
     */
    public static String serialize(Object o) {
        if(objectMapper == null) initialize();
        OutputStream baOutputStream = new ByteArrayOutputStream();
        try {
            objectMapper.writeValue(baOutputStream, o);
        } catch (Exception e) {
            throw new SerializationException(e);
        }
        return baOutputStream.toString();
    }

    /**
     * Deserialize a XML string
     * @param content The XML String object representation
     * @param type The type of the deserialized object instance
     * @return The deserialized object instance
     */
    public static <T> T deserialize(String content, Class<T> type) {
        if(objectMapper == null) initialize();
        try {
            return objectMapper.readValue(content, type);
        } catch (Exception e) {
            throw new SerializationException(e);
        }
    }  
    
    /**
     * Deserialize a XML string
     * @param content The JSON String object representation
     * @param valueTypeRef The typeReference containing the type of the deserialized object instance
     * @return The deserialized object instance
     */
    public static <T> T deserialize(String content, TypeReference valueTypeRef) {
        if(objectMapper == null) initialize();
        try {
            return objectMapper.readValue(content, valueTypeRef);
        } catch (Exception e) {
            throw new SerializationException(e);
        }
    }

}
