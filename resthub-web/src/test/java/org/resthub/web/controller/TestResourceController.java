package org.resthub.web.controller;

import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.junit.Test;
import org.resthub.core.domain.model.Resource;

import com.sun.jersey.api.client.WebResource;

public class TestResourceController extends AbstractWebResthubTest {

    @Test
    public void testGetXML() {
        WebResource r = resource().path("resources");

        String s = r.accept(MediaType.TEXT_XML).get(String.class);
        System.out.print(s + "\n");
        Assert.assertTrue(s.contains("resources"));
    }
    
    @Test
    public void testCreateResource() {
        WebResource r = resource().path("resources");
        String response = r.type("text/xml").post(String.class, new Resource("toto"));
        System.out.print(response + "\n");
        Assert.assertTrue(response.contains("toto"));
    }



}
