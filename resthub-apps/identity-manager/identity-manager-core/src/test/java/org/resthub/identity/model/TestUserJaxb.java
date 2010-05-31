package org.resthub.identity.model;


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

import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Test;

public class TestUserJaxb {

	private User user;
	private User user2;
	private Group group1;
	private Group group2;

	@Before
	public void setUp() {
		user = new User("UserLogin");
		user2 = new User("User2Login");
		group1 = new Group("Group1Name");
		group2 = new Group("Group2Name");

		user.setEmail("test@check.com");
		user.setPassword("TestPassword");
		user.setId(Long.parseLong("1"));
		user.addPermission("perm1");
		user.addPermission("perm2");
		user.addGroup(group1);
		user.addGroup(group2);

		user2.setEmail("test2@check.com");
		user2.setPassword("Test2Password");
	}

	@Test
	public void testUserXMLMarshalling() throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(User.class);
		OutputStream baOutputStream = new ByteArrayOutputStream();
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.marshal(user, baOutputStream);
		System.out.println(baOutputStream.toString());
		assertFalse(baOutputStream.toString().isEmpty());
	}

	@Test
	public void testUserJSONMarshalling() throws JAXBException, JsonMappingException, JsonGenerationException, IOException {
		ObjectMapper mapper = new ObjectMapper();
    	OutputStream baOutputStream = new ByteArrayOutputStream();
   	    AnnotationIntrospector introspector = new JaxbAnnotationIntrospector();
   	    mapper.getDeserializationConfig().setAnnotationIntrospector(introspector);
   	    mapper.getSerializationConfig().setAnnotationIntrospector(introspector);
   	    mapper.writeValue( baOutputStream, user );
        System.out.println(baOutputStream.toString());
        assertFalse(baOutputStream.toString().isEmpty());
	}
}
