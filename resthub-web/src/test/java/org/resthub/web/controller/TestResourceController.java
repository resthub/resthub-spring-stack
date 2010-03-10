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
        Resource res = r.type(MediaType.APPLICATION_XML).post(Resource.class, new Resource());
        Assert.assertNotNull("Unable to create resource", res.getId());
    }
    
    @Test
    public void testFindAllResources() {
    	WebResource r = resource().path("resources");
    	r.type(MediaType.APPLICATION_XML).post(new Resource());
    	r.type(MediaType.APPLICATION_XML).post(new Resource());
        String response = r.type(MediaType.APPLICATION_XML).get(String.class);
        Assert.assertTrue(response.contains("<resources"));
    }
    
    @Test
    public void testFindResource() {
        WebResource r = resource().path("resources");
        Resource res = r.type(MediaType.APPLICATION_XML).post(Resource.class, new Resource());
        Assert.assertNotNull("Resource not created", res);
        
        r = resource().path("resources/" + res.getId());
        res = r.accept(MediaType.APPLICATION_XML).get(Resource.class);
        Assert.assertNotNull("Unable to find resource", res);
    }

    @Test
    public void testDeleteResource() {
        WebResource r = resource().path("resources");
        Resource res = r.type(MediaType.APPLICATION_XML).post(Resource.class, new Resource());
        Assert.assertNotNull("Resource not created", res);
        
        r = resource().path("resources/" + res.getId());
        ClientResponse response = r.delete(ClientResponse.class);
        Assert.assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatus());
        response = r.accept(MediaType.APPLICATION_XML).get(ClientResponse.class);
        Assert.assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }
}
