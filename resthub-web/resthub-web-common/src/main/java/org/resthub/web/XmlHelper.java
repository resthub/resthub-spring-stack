package org.resthub.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.resthub.web.exception.SerializationException;
import org.springframework.data.domain.Page;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 * Helper for XML serialization and deserialization.
 */
public class XmlHelper {

    /**
     * Jackson Object Mapper used to serialization/deserialization
     */
    protected static ObjectMapper objectMapper;

    protected static void initialize() {
        JacksonXmlModule xmlModule = new JacksonXmlModule();
        xmlModule.setDefaultUseWrapper(false);
        objectMapper = new XmlMapper(xmlModule);
        SimpleModule module = new SimpleModule();
        module.addAbstractTypeMapping(Page.class, PageResponse.class);
        objectMapper.registerModule(module);
        AnnotationIntrospector introspector = new JacksonAnnotationIntrospector();
        objectMapper.setAnnotationIntrospector(introspector);
        objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
    }

    /**
     * @return  the objectMapper. It can be used to customize serialization/deserialization configuration.
     */
    public ObjectMapper getObjectMapper() {
        if (objectMapper == null)
            initialize();
        return objectMapper;
    }

    /**
     * Serialize and object to an XML String representation
     * @param o The object to serialize
     * @return The XML String representation
     */
    public static String serialize(Object o) {
        if (objectMapper == null)
            initialize();
        OutputStream baOutputStream = new ByteArrayOutputStream();
        try {
            objectMapper.writeValue(baOutputStream, o);
        } catch (Exception e) {
            throw new SerializationException(e);
        }
        return baOutputStream.toString();
    }

    /**
     * Serialize and object to a JSON String representation with a Jackson view
     * @param o The object to serialize
     * @param view The Jackson view to use
     * @return The JSON String representation
     */
    public static String serialize(Object o, Class<?> view) {
        if (objectMapper == null)
            initialize();
        OutputStream baOutputStream = new ByteArrayOutputStream();
        try {
            ObjectWriter writter = objectMapper.writerWithView(view);
            writter.writeValue(baOutputStream, o);
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
        if (objectMapper == null)
            initialize();
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
        if (objectMapper == null)
            initialize();
        try {
            return objectMapper.readValue(content, valueTypeRef);
        } catch (Exception e) {
            throw new SerializationException(e);
        }
    }

}
