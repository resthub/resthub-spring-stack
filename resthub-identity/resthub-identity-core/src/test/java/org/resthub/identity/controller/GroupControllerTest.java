package org.resthub.identity.controller;

import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.junit.Test;
import org.resthub.core.service.GenericResourceService;
import org.resthub.identity.model.Group;
import org.resthub.identity.model.User;
import org.resthub.identity.service.GroupService;
import org.resthub.web.controller.GenericController;
import org.resthub.web.controller.GenericResourceController;
import org.resthub.web.test.controller.AbstractResourceControllerTest;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.ClientResponse.Status;

/**
 * 
 * @author Guillaume Zurbach
 */
public class GroupControllerTest
		extends
		AbstractResourceControllerTest<Group, GenericResourceController<Group, GenericResourceService<Group>>> {
	/*
	 * @Override public void setUp() throws Exception { super.setUp();
	 * Thread.sleep(100000); }
	 */
	@Inject
	@Named("groupController")
	public void setController(GenericResourceController groupController) {
		super.setController(groupController);
	}

	GroupService groupService;

	@Inject
	@Named("groupService")
	public void setGrouPService(GroupService gs) {
		this.groupService = gs;
	}

	@Test
	public void testCreateGroupWithUser() {
		User u = new User();
		u.setLogin("user1");

		String groupName = "groupName55";

		Group g = new Group();
		g.setName(groupName);
		g.addUser(u);
		GroupController gc = (GroupController) this.controller;
		gc.create(g);

		Group g2 = this.groupService.findByName(groupName);
		if (g2 != null) {
			if (g2.getUsers() != null) {
				for(User u2 : g2.getUsers()) {
					System.out.println(u2.getId());

				}

			}
		}

		assertNotNull(g);
		assertNotNull(g.getUsers());
		assertNotNull(g.getUsers().get(0));

	}

	@Override
	public void testUpdate() throws Exception {
		Group u1 = new Group();
		u1.setName("u1");

		WebResource r = resource().path("group");
		u1 = r.type(MediaType.APPLICATION_XML).post(Group.class, u1);
		r = resource().path("group/" + u1.getId());
		Group u2 = u1;
		u2.setName("u2");
		// Update name
		ClientResponse cr = r.type(MediaType.APPLICATION_XML).accept(
				MediaType.APPLICATION_JSON).put(ClientResponse.class, u2);
		Assert.assertEquals("Group not updated",
				Status.CREATED.getStatusCode(), cr.getStatus());
		String response = resource().path("group").accept(
				MediaType.APPLICATION_JSON).get(String.class);
		Assert.assertFalse("Group not updated", response.contains("u1"));
		Assert.assertTrue("Group not updated", response.contains("u2"));
	}

	@Inject
	@Named("groupController")
	@Override
	@SuppressWarnings("unchecked")
	public void setController(GenericController controller) {
		super.setController(controller);
	}

}
