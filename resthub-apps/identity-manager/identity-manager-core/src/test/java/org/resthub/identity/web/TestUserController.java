package org.resthub.identity.web;

import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.junit.Test;
import org.resthub.identity.domain.model.User;
import org.resthub.web.test.AbstractWebResthubTest;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.ClientResponse.Status;

public class TestUserController extends AbstractWebResthubTest {
    
    @Test
    public void testCreateUser() {
        WebResource r = resource().path("users");
        String response = r.type(MediaType.APPLICATION_XML).post(String.class, new User("u1"));
        System.out.print(response + "\n");
        Assert.assertTrue(response.contains("u1"));
        Assert.assertTrue(response.contains("<user>"));
    }
    
    
    @Test
    public void testFindAllUsers() {
    	WebResource r = resource().path("users");
    	r.type(MediaType.APPLICATION_XML).post(String.class, new User("u1"));
    	r.type(MediaType.APPLICATION_XML).post(String.class, new User("u2"));
    	r.type(MediaType.APPLICATION_XML).post(String.class, new User("u3"));
        String response = r.type(MediaType.APPLICATION_XML).get(String.class);
        System.out.print(response + "\n");
        Assert.assertTrue(response.contains("u1"));
        Assert.assertTrue(response.contains("u2"));
        Assert.assertTrue(response.contains("u3"));
    }
    
    @Test
    public void testFindUserById() {
        WebResource r = resource().path("users");
        User u1 = r.type(MediaType.APPLICATION_XML).post(User.class, new User("u1"));
        r = resource().path("users/" + u1.getId());
        String s = r.accept(MediaType.APPLICATION_XML).get(String.class);
        System.out.print(s + "\n");
        Assert.assertTrue(s.contains("u1"));
    }

    @Test
    public void testDeleteResource() {
        WebResource r = resource().path("users");
        User u1 = r.type(MediaType.APPLICATION_XML).post(User.class, new User("u1"));
        r = resource().path("users/" + u1.getId());
        ClientResponse response = r.delete(ClientResponse.class);
        Assert.assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatus());
        response = r.accept(MediaType.APPLICATION_XML).get(ClientResponse.class);
        Assert.assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }
    
    @Test
    public void testUpdateUser() {
        WebResource r = resource().path("users");
        User u1 = r.type(MediaType.APPLICATION_XML).post(User.class, new User("u1"));
        r.type(MediaType.APPLICATION_XML).post(User.class, new User("u2"));
        r = resource().path("users/" + u1.getId());
        User u3 = u1;
        u3.setLogin("u3");
        ClientResponse cr = r.type(MediaType.APPLICATION_XML).put(ClientResponse.class, u3);
        Assert.assertEquals(Status.CREATED.getStatusCode(), cr.getStatus());
        String s = resource().path("users").accept(MediaType.APPLICATION_XML).get(String.class);
        Assert.assertFalse(s.contains("u1"));
        Assert.assertTrue(s.contains("u2"));
        Assert.assertTrue(s.contains("u3"));

    }

}
