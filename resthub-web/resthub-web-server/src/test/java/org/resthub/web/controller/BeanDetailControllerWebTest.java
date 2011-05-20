package org.resthub.web.controller;

import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.junit.Test;
import org.resthub.web.test.AbstractWebTest;

import com.sun.jersey.api.client.WebResource;

public class BeanDetailControllerWebTest extends AbstractWebTest {
    
    @Test
    public void testFindAllBeans() {
    	WebResource r = resource().path("beans");
        String response = r.type(MediaType.APPLICATION_XML).get(String.class);
        Assert.assertTrue(response.contains("<type>org.resthub.web.controller.BeanDetailsController</type>"));
    }

}
