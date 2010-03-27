package org.resthub.identity.domain.model;

import static org.junit.Assert.assertFalse;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.Before;
import org.junit.Test;

public class TestUserJaxb {
	
	private User user;

    @Before
    public void setUp() {
    	user = new User("TestPersistName");
    }

    @Test
    public void testUserMarshalling() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(User.class);
        OutputStream baOutputStream = new ByteArrayOutputStream();
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.marshal(user, baOutputStream);
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        System.out.println(baOutputStream.toString());
        assertFalse(baOutputStream.toString().isEmpty());
    }
}
