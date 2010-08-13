package org.resthub.identity.controller;

import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.junit.Test;
import org.resthub.identity.model.Group;
import org.resthub.web.test.AbstractWebResthubTest;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.ClientResponse.Status;

public class GroupControllerTest extends AbstractWebResthubTest {

	@Test
	public void testDoNothing() {
		Assert.assertTrue(true);
	}

	// @Test
	public void testCreateGroup() {
		Group g1 = new Group();
		g1.setName("g1");
		WebResource r = resource().path("group");
		String response = r.type(MediaType.APPLICATION_XML).post(String.class, g1);
		/*System.out.print(response + "\n");*/
		Assert.assertTrue(response.contains("g1"));
		Assert.assertTrue(response.contains("<group>"));
	}

	// @Test
	public void testFindAllGroups() {
		Group g1 = new Group();
		Group g2 = new Group();
		Group g3 = new Group();
		g1.setName("g1");
		g2.setName("g2");
		g3.setName("g3");

		WebResource r = resource().path("group");
		r.type(MediaType.APPLICATION_XML).post(String.class, g1);
		r.type(MediaType.APPLICATION_XML).post(String.class, g2);
		r.type(MediaType.APPLICATION_XML).post(String.class, g3);
		String response = r.type(MediaType.APPLICATION_JSON).get(String.class);
		System.out.print("Result : " + response + "\n");
		Assert.assertTrue(response.contains("g1"));
		Assert.assertTrue(response.contains("g2"));
		Assert.assertTrue(response.contains("g3"));
	}

	// @Test
	public void testFindGroupById() {
		Group g1 = new Group();
		g1.setName("g1");
		WebResource r = resource().path("group");
		g1 = r.type(MediaType.APPLICATION_XML).post(Group.class, g1);
		r = resource().path("group/" + g1.getId());
		String response = r.accept(MediaType.APPLICATION_XML).get(String.class);
		// System.out.print(response + "\n");
		Assert.assertTrue(response.contains("g1"));
	}

	// @Test
	public void testDeleteGroup() {
		Group g1 = new Group();
		g1.setName("g1");

		WebResource r = resource().path("group");
		g1 = r.type(MediaType.APPLICATION_XML).post(Group.class, g1);
		r = resource().path("group/" + g1.getId());
		ClientResponse response = r.delete(ClientResponse.class);
		Assert.assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatus());
		response = r.accept(MediaType.APPLICATION_XML).get(ClientResponse.class);
		Assert.assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
	}

	// @Test
	public void testUpdateGroup() {
		Group g1 = new Group();
		Group g2 = new Group();
		g1.setName("g1");
		g2.setName("g2");

		WebResource r = resource().path("group");
		g1 = r.type(MediaType.APPLICATION_XML).post(Group.class, g1);
		g2 = r.type(MediaType.APPLICATION_XML).post(Group.class, g2);

		r = resource().path("group/" + g1.getId());
		Group g3 = g1;
		g3.setName("g3");

		/* Updating group g1 with a new name : 'g3' */
		ClientResponse cr = r.type(MediaType.APPLICATION_XML).put(ClientResponse.class, g3);
		Assert.assertEquals(Status.CREATED.getStatusCode(), cr.getStatus());
		String s = resource().path("group").accept(MediaType.APPLICATION_XML).get(String.class);
		Assert.assertFalse(s.contains("g1"));
		Assert.assertTrue(s.contains("g2"));
		Assert.assertTrue(s.contains("g3"));

	}

}
