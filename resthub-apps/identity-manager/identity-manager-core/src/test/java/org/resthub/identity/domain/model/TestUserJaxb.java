package org.resthub.identity.domain.model;

import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONMarshaller;
import com.sun.jersey.api.json.JSONJAXBContext;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Test;
import org.resthub.identity.model.Group;
import org.resthub.identity.model.User;

public class TestUserJaxb {

	private User user;
	private Group group1;
	private Group group2;

	@Before
	public void setUp() {
		user = new User("UserLogin");
		group1 = new Group("Group1Name");
		group2 = new Group("Group2Name");

		user.setEmail("test@check.com");
		user.setPassword("TestPassword");
		user.addPermission("perm1");
		user.addPermission("perm2");
		user.addGroup(group1);
		user.addGroup(group2);
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
		JSONJAXBContext jsonJaxbContext = new JSONJAXBContext(JSONConfiguration.badgerFish().build(), User.class);
		OutputStream baOutputStream = new ByteArrayOutputStream();
		JSONMarshaller marshaller = jsonJaxbContext.createJSONMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.marshallToJSON(user, baOutputStream);
		System.out.println(baOutputStream.toString());
		assertFalse(baOutputStream.toString().isEmpty());
	}
}
