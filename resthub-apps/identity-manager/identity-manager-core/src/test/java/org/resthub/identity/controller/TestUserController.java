package org.resthub.identity.controller;

import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.junit.Test;
import org.resthub.identity.model.User;
import org.resthub.web.test.AbstractWebResthubTest;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.ClientResponse.Status;

public class TestUserController extends AbstractWebResthubTest {

	@Test
	public void testCreateUser() {
		User u1 = new User();
		u1.setLogin("u1");
		WebResource r = resource().path("user");
		String response = r.type(MediaType.APPLICATION_XML).post(String.class, u1);
		// System.out.print(response + "\n");
		Assert.assertTrue(response.contains("u1"));
		Assert.assertTrue(response.contains("<user>"));
	}


	@Test
	public void testFindAllUsers() {
		User u1 = new User();
		User u2 = new User();
		User u3 = new User();
		u1.setLogin("u1");
		u1.setLogin("u2");
		u1.setLogin("u3");
		WebResource r = resource().path("user");
		r.type(MediaType.APPLICATION_XML).post(String.class, u1);
		r.type(MediaType.APPLICATION_XML).post(String.class, u2);
		r.type(MediaType.APPLICATION_XML).post(String.class, u3);
		String response = r.type(MediaType.APPLICATION_XML).get(String.class);
		System.out.print(response + "\n");
		Assert.assertTrue(response.contains("u1"));
		Assert.assertTrue(response.contains("u2"));
		Assert.assertTrue(response.contains("u3"));
	}

	@Test
	public void testFindUserById() {
		User u1 = new User();
		u1.setLogin("u1");
		WebResource r = resource().path("user");
		u1 = r.type(MediaType.APPLICATION_XML).post(User.class, u1);
		r = resource().path("user/" + u1.getId());
		String s = r.accept(MediaType.APPLICATION_XML).get(String.class);
		// System.out.print(s + "\n");
		Assert.assertTrue(s.contains("u1"));
	}

	@Test
	public void testDeleteUser() {
		User u1 = new User();
		u1.setLogin("u1");
		WebResource r = resource().path("user");
		u1 = r.type(MediaType.APPLICATION_XML).post(User.class, u1);
		r = resource().path("user/" + u1.getId());
		ClientResponse response = r.delete(ClientResponse.class);
		Assert.assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatus());
		response = r.accept(MediaType.APPLICATION_XML).get(ClientResponse.class);
		Assert.assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
	}

	@Test
	public void testUpdateUser() {
		User u1 = new User();
		User u2 = new User();
		u1.setLogin("u1");
		u1.setLogin("u2");

		WebResource r = resource().path("user");
		u1 = r.type(MediaType.APPLICATION_XML).post(User.class, u1);
		u2 = r.type(MediaType.APPLICATION_XML).post(User.class, u2);
		r = resource().path("user/" + u1.getId());
		User u3 = u1;
		u3.setLogin("u3");
		ClientResponse cr = r.type(MediaType.APPLICATION_XML).put(ClientResponse.class, u3);
		Assert.assertEquals(Status.CREATED.getStatusCode(), cr.getStatus());
		String s = resource().path("user").accept(MediaType.APPLICATION_XML).get(String.class);
		Assert.assertFalse(s.contains("u1"));
		Assert.assertTrue(s.contains("u2"));
		Assert.assertTrue(s.contains("u3"));
	}
}
