package org.resthub.web.marshalling;

import static org.junit.Assert.assertFalse;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.Before;
import org.junit.Test;
import org.resthub.web.model.WebSampleResource;

public class TestWebSampleResourceJaxb {
	
	private WebSampleResource resource;

    @Before
    public void setUp() {
    	resource = new WebSampleResource();
    }

    @Test
    public void testXMLMarshalling() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(WebSampleResource.class);
        OutputStream baOutputStream = new ByteArrayOutputStream();
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.marshal(resource, baOutputStream);
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        assertFalse(baOutputStream.toString().isEmpty());
    }
    
}
