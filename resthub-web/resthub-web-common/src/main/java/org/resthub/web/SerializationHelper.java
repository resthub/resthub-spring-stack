package org.resthub.web;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SerializationHelper {

    private static final Logger logger = LoggerFactory.getLogger(SerializationHelper.class);

    public SerializationHelper() {

    }

    public static String xmlSerialize(Object o) {
        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(o.getClass());
            OutputStream baOutputStream = new ByteArrayOutputStream();
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(o, baOutputStream);
            return baOutputStream.toString();
        } catch (JAXBException e) {
            logger.error(e.toString());
            return "";
        }
    }

    public static Object xmlDeserialize(String content, Class<?> valueType) {
        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(valueType);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            ByteArrayInputStream input = new ByteArrayInputStream(content.getBytes());
            return unmarshaller.unmarshal(input);
        } catch (JAXBException e) {
            logger.error(e.toString());
            return null;
        }
    }

    protected static ObjectMapper getJsonObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        AnnotationIntrospector introspector = new JacksonAnnotationIntrospector();

        mapper.getSerializationConfig().disable(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS);
        mapper.getDeserializationConfig().disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.getSerializationConfig().enable(SerializationConfig.Feature.INDENT_OUTPUT);

        mapper.getDeserializationConfig().setAnnotationIntrospector(introspector);
        mapper.getSerializationConfig().setAnnotationIntrospector(introspector);

        return mapper;
    }

    public static String jsonSerialize(Object o) {
        ObjectMapper mapper = getJsonObjectMapper();
        OutputStream baOutputStream = new ByteArrayOutputStream();
        try {
            mapper.writeValue(baOutputStream, o);
        } catch (Exception e) {
            logger.error(e.toString());
            return "";
        }
        return baOutputStream.toString();
    }

    public static Object jsonDeserialize(String content, Class<?> valueType) {
        ObjectMapper mapper = getJsonObjectMapper();
        try {
            return mapper.readValue(content, valueType);
        } catch (Exception e) {
            logger.error(e.toString());
            return null;
        }
    }

}
