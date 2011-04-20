package org.resthub.identity.model;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import junit.framework.Assert;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonProcessingException;
import org.hibernate.util.SerializationHelper;
import org.junit.Test;

public class UserSerializationTest {

	protected User createTestUser() {
		User user;
		Group group1;
		Group group2;

		user = new User();
		group1 = new Group();
		group2 = new Group();

		user.setId(Long.parseLong("1"));
		user.setLogin("UserLogin");
		user.setEmail("test@check.com");
		user.setPassword("TestPassword");
		user.getPermissions().add("perm1");
		user.getPermissions().add("perm2");

		group1.setName("Group1Name");
		group2.setName("Group2Name");

		user.getGroups().add(group1);
		user.getGroups().add(group2);
		
		return user;
	}
	
	/**@Test
	public void testUserJacksonSerialization() throws JsonGenerationException, JsonProcessingException, IOException {
		User user = createTestUser();
		String output = SerializationHelper.jsonSerialize(user);
		Assert.assertTrue(output.contains("perm1"));
	}
	
	@Test
	public void testUserXmlSerialization() throws JAXBException {
		User user = createTestUser();
		String output = SerializationHelper.xmlSerialize(user);
		Assert.assertTrue(output.contains("perm1"));
	}*/
	
}