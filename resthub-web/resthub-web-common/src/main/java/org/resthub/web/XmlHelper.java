package org.resthub.web;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.resthub.web.exception.SerializationException;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * Helper for XML serialization and deserialization
 */
public class XmlHelper {
    
    protected static ObjectMapper getXmlObjectMapper() {
        ObjectMapper mapper = new XmlMapper();
        AnnotationIntrospector introspector = new JacksonAnnotationIntrospector();
        mapper.setAnnotationIntrospector(introspector);
        return mapper;
    }

    /**
     * Serialize and object to an XML String representation
     * @param o The object to serialize
     * @return The XML String representation
     */
    public static String serialize(Object o) {
        ObjectMapper mapper = getXmlObjectMapper();
        OutputStream baOutputStream = new ByteArrayOutputStream();
        try {
            mapper.writeValue(baOutputStream, o);
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
        ObjectMapper mapper = getXmlObjectMapper();
        try {
            return type.cast(mapper.readValue(content, type));
        } catch (Exception e) {
            throw new SerializationException(e);
        }
    }

   

}
