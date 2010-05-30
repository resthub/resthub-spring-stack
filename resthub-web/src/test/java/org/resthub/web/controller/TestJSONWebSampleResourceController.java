package org.resthub.web.controller;

import com.sun.jersey.api.client.WebResource;
import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.junit.Test;
import org.resthub.core.model.Resource;
import org.resthub.web.model.WebSampleResource;
import org.resthub.web.test.AbstractWebResthubTest;


public class TestJSONWebSampleResourceController extends AbstractWebResthubTest {
    
    @Test
    public void testCreateResource() {

        WebResource r = resource().path("resources");
        Resource res = r.type(MediaType.APPLICATION_JSON).post(WebSampleResource.class, new WebSampleResource());
        Assert.assertNotNull("Unable to create resource", res.getId());
    }

    @Test
    public void testFindAllResources() {
    	WebResource r = resource().path("resources");
    	r.type(MediaType.APPLICATION_JSON).post(new WebSampleResource());
    	r.type(MediaType.APPLICATION_JSON).post(new WebSampleResource());
        String response = r.type(MediaType.APPLICATION_JSON).get(String.class);
        System.out.println(response);
    }
    
}
