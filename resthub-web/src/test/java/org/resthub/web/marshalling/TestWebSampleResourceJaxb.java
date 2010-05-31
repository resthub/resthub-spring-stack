package org.resthub.web.marshalling;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBException;

import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;

import org.resthub.web.model.WebSampleResource;

public class TestWebSampleResourceJaxb {
	
	private WebSampleResource resource;

    @Before
    public void setUp() {
    	resource = new WebSampleResource();
    }

    @Test
    public void testXMLMarshalling() throws JsonMappingException, JsonGenerationException, IOException {
		ObjectMapper mapper = new ObjectMapper();
    	OutputStream baOutputStream = new ByteArrayOutputStream();
   	    AnnotationIntrospector introspector = new JaxbAnnotationIntrospector();
   	    mapper.getDeserializationConfig().setAnnotationIntrospector(introspector);
   	    mapper.getSerializationConfig().setAnnotationIntrospector(introspector);
   	    mapper.writeValue( baOutputStream, resource );
        System.out.println(baOutputStream.toString());
        assertFalse(baOutputStream.toString().isEmpty());
    }

	@Test
	public void testJSONMarshalling() throws JAXBException, JsonMappingException, JsonGenerationException, IOException {
		ObjectMapper mapper = new ObjectMapper();
    	OutputStream baOutputStream = new ByteArrayOutputStream();
   	    AnnotationIntrospector introspector = new JaxbAnnotationIntrospector();
   	    mapper.getDeserializationConfig().setAnnotationIntrospector(introspector);
   	    mapper.getSerializationConfig().setAnnotationIntrospector(introspector);
   	    mapper.writeValue( baOutputStream, resource );
        System.out.println(baOutputStream.toString());
        assertFalse(baOutputStream.toString().isEmpty());
	}
}
