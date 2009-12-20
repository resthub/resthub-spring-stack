package org.resthub.web.marshalling;

import static org.junit.Assert.assertFalse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;
import org.junit.Before;
import org.junit.Test;
import org.resthub.core.domain.model.Resource;

public class TestResourceJaxb {
	
	private Resource resource;

    @Before
    public void setUp() {
    	resource = new Resource("TestPersistName");
    }

    @Test
    public void testUserXMLMarshalling() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Resource.class);
        OutputStream baOutputStream = new ByteArrayOutputStream();
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.marshal(resource, baOutputStream);
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        System.out.println(baOutputStream.toString());
        assertFalse(baOutputStream.toString().isEmpty());
    }
    
    @Test
    public void testUserJSONMarshalling() throws JsonGenerationException, JsonMappingException, IOException  {
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
