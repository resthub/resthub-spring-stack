package org.resthub.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;

public class SerializationHelper {
	
	public SerializationHelper() {
	    
	}
	
	public static String xmlSerialize(Object o) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(o.getClass());
		OutputStream baOutputStream = new ByteArrayOutputStream();
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.marshal(o, baOutputStream);
		return baOutputStream.toString();
	}
	
	public static String jsonSerialize(Object o) throws JsonGenerationException, JsonProcessingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		AnnotationIntrospector introspector = new JacksonAnnotationIntrospector();
	    
	    mapper.getSerializationConfig().disable(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS);
	    mapper.getDeserializationConfig().disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
	    mapper.getSerializationConfig().enable(SerializationConfig.Feature.INDENT_OUTPUT);

	    mapper.getDeserializationConfig().setAnnotationIntrospector(introspector);
	    mapper.getSerializationConfig().setAnnotationIntrospector(introspector);
		
		OutputStream baOutputStream = new ByteArrayOutputStream();
		mapper.writeValue( baOutputStream, o );
	    return baOutputStream.toString();
	}


}
