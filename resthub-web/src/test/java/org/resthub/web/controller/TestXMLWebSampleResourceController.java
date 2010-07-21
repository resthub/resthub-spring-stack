package org.resthub.web.controller;

import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.junit.Test;
import org.resthub.core.model.Resource;
import org.resthub.web.model.WebSampleResource;
import org.resthub.web.test.AbstractWebResthubTest;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.ClientResponse.Status;

public class TestXMLWebSampleResourceController extends AbstractWebResthubTest {
    
    @Test
    public void testCreateResource() {
        WebResource r = resource().path("resources");
        Resource res = r.type(MediaType.APPLICATION_XML).post(WebSampleResource.class, new WebSampleResource("test"));
        Assert.assertNotNull("Unable to create resource", res.getId());
    }
    
    @Test
    public void testFindAllResources() {
    	WebResource r = resource().path("resources");
    	r.type(MediaType.APPLICATION_XML).post(WebSampleResource.class, new WebSampleResource("A"));
    	r.type(MediaType.APPLICATION_XML).post(WebSampleResource.class, new WebSampleResource("B"));
        String response = r.accept(MediaType.APPLICATION_XML).get(String.class);
        Assert.assertTrue(response.contains("<pageResponse>"));
        Assert.assertTrue(response.contains("<totalElements>2</totalElements>"));
    }
    
    @Test
    public void testFindResource() {
        WebResource r = resource().path("resources");
        Resource res = r.type(MediaType.APPLICATION_XML).post(WebSampleResource.class, new WebSampleResource("test"));
        Assert.assertNotNull("Resource not created", res);
        
        r = resource().path("resources/" + res.getId());
        res = r.accept(MediaType.APPLICATION_XML).get(WebSampleResource.class);
        Assert.assertNotNull("Unable to find resource", res);
    }

    @Test
    public void testDeleteResource() {
        WebResource r = resource().path("resources");
        Resource res = r.type(MediaType.APPLICATION_XML).post(WebSampleResource.class, new WebSampleResource("test"));
        Assert.assertNotNull("Resource not created", res);
        
        r = resource().path("resources/" + res.getId());
        ClientResponse response = r.delete(ClientResponse.class);
        Assert.assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatus());
        response = r.accept(MediaType.APPLICATION_XML).get(ClientResponse.class);
        Assert.assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }
}
