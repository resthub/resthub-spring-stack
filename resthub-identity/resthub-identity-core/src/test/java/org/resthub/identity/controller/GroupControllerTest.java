package org.resthub.identity.controller;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.ClientResponse.Status;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.MediaType;
import junit.framework.Assert;
import org.resthub.core.service.GenericResourceService;
import org.resthub.identity.model.Group;
import org.resthub.web.controller.GenericResourceController;
import org.resthub.web.test.AbstractResourceControllerTest;


/**
 *
 * @author Guillaume Zurbach
 */
public class GroupControllerTest extends AbstractResourceControllerTest<Group, GenericResourceController<Group, GenericResourceService<Group>>> {

	@Inject
	@Named("groupController")
	public void setController(GenericResourceController groupController) {
		super.setController(groupController);
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
		ClientResponse cr = r.type(MediaType.APPLICATION_XML)
							 .accept(MediaType.APPLICATION_JSON)
							 .put(ClientResponse.class, u2);
		Assert.assertEquals("Group not updated", Status.CREATED.getStatusCode(), cr.getStatus());
		String response = resource().path("group")
									.accept(MediaType.APPLICATION_JSON)
									.get(String.class);
		Assert.assertFalse("Group not updated", response.contains("u1"));
		Assert.assertTrue("Group not updated", response.contains("u2"));
	}
}
