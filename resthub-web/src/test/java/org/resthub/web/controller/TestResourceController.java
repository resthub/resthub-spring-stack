package org.resthub.web.controller;

import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.junit.Test;
import org.resthub.core.domain.model.Resource;
import org.resthub.web.test.AbstractWebResthubTest;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.ClientResponse.Status;

public class TestResourceController extends AbstractWebResthubTest {
    
    @Test
    public void testCreateResource() {
        WebResource r = resource().path("resources");
        String response = r.type(MediaType.APPLICATION_XML).post(String.class, new Resource("r1"));
        System.out.print(response + "\n");
        Assert.assertTrue(response.contains("r1"));
    }
    
    
    @Test
    public void testFindAllResource() {
    	WebResource r = resource().path("resources");
    	r.type(MediaType.APPLICATION_XML).post(String.class, new Resource("r1"));
    	r.type(MediaType.APPLICATION_XML).post(String.class, new Resource("r2"));
    	r.type(MediaType.APPLICATION_XML).post(String.class, new Resource("r3"));
        String response = r.type(MediaType.APPLICATION_XML).get(String.class);
        System.out.print(response + "\n");
        Assert.assertTrue(response.contains("r1"));
        Assert.assertTrue(response.contains("r2"));
        Assert.assertTrue(response.contains("r3"));
    }
    
    @Test
    public void testFindResourceByName() {
        WebResource r = resource().path("resources");
        r.type(MediaType.APPLICATION_XML).post(String.class, new Resource("r1"));
        r = resource().path("resources/r1");
        String s = r.accept(MediaType.APPLICATION_XML).get(String.class);
        System.out.print(s + "\n");
        Assert.assertTrue(s.contains("r1"));
    }

    @Test
    public void testDeleteResource() {
        WebResource r = resource().path("resources");
        r.type(MediaType.APPLICATION_XML).post(String.class, new Resource("r1"));
        r = resource().path("resources/r1");
        ClientResponse response = r.delete(ClientResponse.class);
        Assert.assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatus());
        response = r.accept(MediaType.APPLICATION_XML).get(ClientResponse.class);
        Assert.assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }
    
    @Test
    public void testUpdateResource() {
        WebResource r = resource().path("resources");
        r.type(MediaType.APPLICATION_XML).post(String.class, new Resource("r1"));
        r.type(MediaType.APPLICATION_XML).post(String.class, new Resource("r2"));
        r = resource().path("resources/r1");
        ClientResponse cr = r.type(MediaType.APPLICATION_XML).put(ClientResponse.class, new Resource("r3"));
        Assert.assertEquals(Status.CREATED.getStatusCode(), cr.getStatus());
        String s = resource().path("resources").accept(MediaType.APPLICATION_XML).get(String.class);
        Assert.assertFalse(s.contains("r1"));
        Assert.assertTrue(s.contains("r2"));
        Assert.assertTrue(s.contains("r3"));

    }

}
