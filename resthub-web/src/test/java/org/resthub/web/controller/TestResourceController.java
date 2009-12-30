package org.resthub.web.controller;

import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.resthub.core.domain.model.Resource;

import com.sun.jersey.api.client.WebResource;

public class TestResourceController extends AbstractWebResthubTest {
	
	@Before
	public void init() {
		
	}

//    @Test
//    public void testGetXML() {
//        WebResource r = resource().path("resources");
//
//        String s = r.accept(MediaType.TEXT_XML).get(String.class);
//        System.out.print(s + "\n");
//        Assert.assertTrue(s.contains("resources"));
//    }
    
    @Test
    public void testCreateResource() {
        WebResource r = resource().path("resources");
        r.type("text/xml").put(new Resource("toto"));

        String s = r.queryParam("name", "toto").accept(MediaType.TEXT_XML).get(String.class);
        System.out.print(s + "\n");
        Assert.assertTrue(s.contains("toto"));
    }



}
