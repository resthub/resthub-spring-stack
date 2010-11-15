package org.resthub.identity.controller;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.junit.Test;
import org.resthub.core.service.GenericResourceService;
import org.resthub.identity.model.User;
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
public class UserControllerTest extends AbstractResourceControllerTest<User, GenericResourceController<User, GenericResourceService<User>>> {

	@Inject
	@Named("userController")
	public void setController(GenericResourceController userController) {
		super.setController(userController);
	}
	
	@Test
	public void testMultiDeletion() throws Exception{
		User u1 = new User();
		u1.setLogin("u1");
		
		User u2 = new User();
		u2.setLogin("u2");
		
		WebResource r1 = resource().path("user");
		u1 = r1.type(MediaType.APPLICATION_XML).post(User.class, u1);
		Assert.assertNotNull("Resource not created", r1);

		
		WebResource r2 = resource().path("user");
		u2 = r2.type(MediaType.APPLICATION_XML).post(User.class, u2);
		Assert.assertNotNull("Resource not created", r2);

		
		r1 = resource().path(getResourcePath() + "/" + getIdFromObject(u1));
		r2 = resource().path(getResourcePath() + "/" + getIdFromObject(u2));
		

		myRunnable a=new myRunnable();
		a.setResource(r1);
		myRunnable b=new myRunnable();
		b.setResource(r2);
		Thread t1=new Thread(a);
		Thread t2=new Thread(b);
		
		t1.start();
		t2.start();
		
		
		
	}
		
	private class myRunnable implements Runnable{

		WebResource r;
		public void setResource(WebResource r){
			this.r=r;
		}
		@Override
		public void run() {
			ClientResponse response = r.delete(ClientResponse.class);
			Assert.assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatus());
			response = r.accept(MediaType.APPLICATION_XML).get(ClientResponse.class);
			Assert.assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
			System.out.println("Deletion OK");
		}
	};


	@Override
	public void testUpdate() throws Exception {
		User u1 = new User();
		u1.setLogin("u1");

		WebResource r = resource().path("user");
		u1 = r.type(MediaType.APPLICATION_XML).post(User.class, u1);
		r = resource().path("user/" + u1.getId());
		User u2 = u1;
		u2.setLogin("u2");
		// Update login
		ClientResponse cr = r.type(MediaType.APPLICATION_XML)
							 .accept(MediaType.APPLICATION_JSON)
							 .put(ClientResponse.class, u2);
		Assert.assertEquals("User not updated", Status.CREATED.getStatusCode(), cr.getStatus());
		String response = resource().path("user")
									.accept(MediaType.APPLICATION_JSON)
									.get(String.class);
		Assert.assertFalse("User not updated", response.contains("u1"));
		Assert.assertTrue("User not updated", response.contains("u2"));
	}
	
	 @Inject
	    @Named("userController")
	    @Override
	    @SuppressWarnings("unchecked")
	    public void setController(GenericController controller) {
	        super.setController(controller);
	    }
	    
	  
}
