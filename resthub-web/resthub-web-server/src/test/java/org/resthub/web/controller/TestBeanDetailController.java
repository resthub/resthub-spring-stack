package org.resthub.web.controller;

import javax.ws.rs.core.MediaType;

import org.junit.Test;
import org.resthub.web.test.AbstractWebTest;

import com.sun.jersey.api.client.WebResource;

public class TestBeanDetailController extends AbstractWebTest {
    
    @Test
    public void testFindAllBeans() {
    	WebResource r = resource().path("beans");
        String response = r.type(MediaType.APPLICATION_XML).get(String.class);
        System.out.print(response + "\n");
    }

}
