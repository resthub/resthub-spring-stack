package org.resthub.web.controller;

import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.junit.Test;

import com.sun.jersey.api.client.WebResource;

public class TestResourceController extends AbstractWebResthubTest {

    @Test
    public void testGetXML() {
        WebResource r = resource().path("resources");

        String s = r.accept(MediaType.TEXT_XML).get(String.class);
        System.out.print(s + "\n");
        Assert.assertTrue(s.contains("TestPersistName"));
    }
    
    @Test
    public void testGetJSON() {
        WebResource r = resource().path("resources");

        String s = r.accept(MediaType.APPLICATION_JSON).get(String.class);
        System.out.print(s + "\n");
        Assert.assertTrue(s.contains("TestPersistName"));
    }



}
