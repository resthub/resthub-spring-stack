package org.resthub.identity.web;

import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.junit.Test;
import org.resthub.identity.model.Group;
import org.resthub.web.test.AbstractWebResthubTest;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.ClientResponse.Status;

public class TestGroupController extends AbstractWebResthubTest {

    @Test
    public void testCreateGroup() {
        WebResource r = resource().path("group");
        String response = r.type(MediaType.APPLICATION_XML).post(String.class, new Group("g1"));
        System.out.print(response + "\n");
        Assert.assertTrue(response.contains("g1"));
        Assert.assertTrue(response.contains("<group>"));
    }


    @Test
    public void testFindAllGroups() {
    	WebResource r = resource().path("group");
    	r.type(MediaType.APPLICATION_XML).post(String.class, new Group("g1"));
    	r.type(MediaType.APPLICATION_XML).post(String.class, new Group("g2"));
    	r.type(MediaType.APPLICATION_XML).post(String.class, new Group("g3"));
        String response = r.type(MediaType.APPLICATION_XML).get(String.class);
        System.out.print(response + "\n");
        Assert.assertTrue(response.contains("g1"));
        Assert.assertTrue(response.contains("g2"));
        Assert.assertTrue(response.contains("g3"));
    }

    @Test
    public void testFindGroupById() {
        WebResource r = resource().path("group");
        Group g1 = r.type(MediaType.APPLICATION_XML).post(Group.class, new Group("g1"));
        r = resource().path("group/" + g1.getId());
        String response = r.accept(MediaType.APPLICATION_XML).get(String.class);
        System.out.print(response + "\n");
        Assert.assertTrue(response.contains("g1"));
    }

    @Test
    public void testDeleteGroup() {
        WebResource r = resource().path("group");
        Group g1 = r.type(MediaType.APPLICATION_XML).post(Group.class, new Group("g1"));
        r = resource().path("group/" + g1.getId());
        ClientResponse response = r.delete(ClientResponse.class);
        Assert.assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatus());
        response = r.accept(MediaType.APPLICATION_XML).get(ClientResponse.class);
        Assert.assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    public void testUpdateGroup() {
        WebResource r = resource().path("group");
        Group g1 = r.type(MediaType.APPLICATION_XML).post(Group.class, new Group("g1"));
        r.type(MediaType.APPLICATION_XML).post(Group.class, new Group("g2"));
        r = resource().path("group/" + g1.getId());
        Group g3 = g1;
        g3.setName("g3");
        ClientResponse cr = r.type(MediaType.APPLICATION_XML).put(ClientResponse.class, g3);
        Assert.assertEquals(Status.CREATED.getStatusCode(), cr.getStatus());
        String s = resource().path("group").accept(MediaType.APPLICATION_XML).get(String.class);
        Assert.assertFalse(s.contains("g1"));
        Assert.assertTrue(s.contains("g2"));
        Assert.assertTrue(s.contains("g3"));

    }

}
