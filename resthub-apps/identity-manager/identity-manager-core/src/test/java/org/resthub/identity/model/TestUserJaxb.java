package org.resthub.identity.model;

import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONMarshaller;
import com.sun.jersey.api.json.JSONJAXBContext;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Test;
import org.resthub.core.model.Resource;

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
		/*user.addPermission("perm1");
		user.addPermission("perm2");*/
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
	public void testUserJSONMarshalling() throws JAXBException {
		Class<?>[] cTypes = { User.class, Resource.class };
		JSONJAXBContext jsonJaxbContext = new JSONJAXBContext(JSONConfiguration.natural().build(), cTypes);
		OutputStream baOutputStream = new ByteArrayOutputStream();
		JSONMarshaller marshaller = jsonJaxbContext.createJSONMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.marshallToJSON(user, baOutputStream);
		System.out.println(baOutputStream.toString());
		assertFalse(baOutputStream.toString().isEmpty());
	}
}
