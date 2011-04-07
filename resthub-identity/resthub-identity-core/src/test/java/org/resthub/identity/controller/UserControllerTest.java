package org.resthub.identity.controller;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.resthub.identity.model.User;
import org.resthub.identity.service.UserService;
import org.resthub.web.test.controller.AbstractResourceControllerTest;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.ClientResponse.Status;

/**
 * 
 * @author Guillaume Zurbach
 */
public class UserControllerTest extends
		AbstractResourceControllerTest<User, UserService, UserController> {

	Logger logger = Logger.getLogger(UserControllerTest.class);

	
    @Override
	@Inject
	public void setController(UserController userController) {
		super.setController(userController);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected User createTestResource() throws Exception {
		logger.debug("UserControllerTest : createTestResource");
		String userLogin = "UserTestUserLogin"+ Math.round(Math.random() * 1000);
		String userPassword = "UserTestUserPassword";
		User u = new User();
		u.setLogin(userLogin);
		u.setPassword(userPassword);
		return u;
	}

	@Override
	public void testUpdate() throws Exception {
		logger.debug("UserControllerTest : testUpdate : START");
		User u1 = createTestResource();
		String userPassword="UserTestUserPassword";
		
		WebResource r = resource().path("user");
		logger.debug("UserControllerTest : testUpdate : GonnaPost");	
		u1 = r.type(MediaType.APPLICATION_XML).post(User.class, u1);
		logger.debug("UserControllerTest : testUpdate : DidPost");
		r = resource().path("user/" + u1.getId());
		User u2 = u1;
		u2.setPassword(userPassword);
		u2.setLogin("u2");
		// Update login
		ClientResponse cr = r.type(MediaType.APPLICATION_XML).accept(
				MediaType.APPLICATION_JSON).put(ClientResponse.class, u2);
		Assert.assertEquals("User not updated", Status.CREATED.getStatusCode(),
				cr.getStatus());
		String response = resource().path("user").accept(
				MediaType.APPLICATION_JSON).get(String.class);
		Assert.assertFalse("User not updated", response.contains("u1"));
		Assert.assertTrue("User not updated", response.contains("u2"));
	}

}
