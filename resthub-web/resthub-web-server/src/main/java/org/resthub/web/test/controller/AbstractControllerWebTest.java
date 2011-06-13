package org.resthub.web.test.controller;

import java.io.Serializable;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Test;
import org.resthub.web.test.AbstractWebTest;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * Base class for your generic controller integration tests Run an embeded
 * servlet container in order to test your controller
 */
public abstract class AbstractControllerWebTest<T, ID extends Serializable> extends AbstractWebTest {

    /**
     * Returns the resource path of controller class
     */
    protected abstract String getResourcePath();

    /**
     * Creates a generic instance for tests
     * 
     * @return T
     */
    protected abstract T createTestResource();

    protected abstract T udpateTestResource(T r);

    /**
     * Creates a generic instance for tests
     * 
     * @return T
     */
    protected abstract ID getResourceId(T resource);

    /**
     * Cleans all persisted objects to simulate transactionnal tests
     */
    @After
    public void cleanAll() {
        resource().path(getResourcePath() + "/all").delete();
    }

    // -------------------------------------------------------------------------
    // Tests methods

    @Test
    @SuppressWarnings("unchecked")
    public void testCreateResource() {
        WebResource wr = resource().path(getResourcePath());
        T r = createTestResource();
        T res = (T) wr.type(MediaType.APPLICATION_XML).post(r.getClass(), r);
        Assert.assertNotNull("Resource not created", res);
    }

    @Test
    public void testFindAllResourcesJson() {
        WebResource wr = resource().path(getResourcePath());
        wr.type(MediaType.APPLICATION_JSON).post(String.class, createTestResource());
        wr.type(MediaType.APPLICATION_JSON).post(String.class, createTestResource());
        String response = wr.accept(MediaType.APPLICATION_JSON).get(String.class);

        Assert.assertTrue("Unable to find all resources or bad-formed JSON", response.contains("\"totalElements\" : 2"));
    }

    @Test
    public void testFindAllResourcesXml() {
        WebResource wr = resource().path(getResourcePath());
        wr.type(MediaType.APPLICATION_XML).post(String.class, createTestResource());
        wr.type(MediaType.APPLICATION_XML).post(String.class, createTestResource());
        String response = wr.accept(MediaType.APPLICATION_XML).get(String.class);

        Assert.assertTrue("Unable to find all resources or bad-formed XML",
                response.contains("<totalElements>2</totalElements>"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testDeleteResource() {
        WebResource wr = resource().path(getResourcePath());
        T r = createTestResource();

        T res = (T) wr.type(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML).post(r.getClass(), r);
        Assert.assertNotNull("Resource not created", res);

        wr = resource().path(getResourcePath() + "/" + getResourceId(res));
        ClientResponse response = wr.delete(ClientResponse.class);
        Assert.assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatus());
        response = wr.accept(MediaType.APPLICATION_XML).get(ClientResponse.class);
        Assert.assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testFindResource() {
        WebResource wr = resource().path(getResourcePath());
        T r = createTestResource();
        T res = (T) wr.type(MediaType.APPLICATION_XML).post(r.getClass(), r);
        Assert.assertNotNull("Resource not created", res);

        wr = resource().path(getResourcePath() + "/" + getResourceId(res));
        ClientResponse cr = wr.get(ClientResponse.class);
        Assert.assertEquals("Unable to find resource", Status.OK.getStatusCode(), cr.getStatus());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testUpdate() {
        T r1 = createTestResource();
        r1 = (T) resource().path(getResourcePath()).type(MediaType.APPLICATION_XML).post(r1.getClass(), r1);
        WebResource wr = resource().path(getResourcePath() + "/" + this.getResourceId(r1));
        String response1 = wr.accept(MediaType.APPLICATION_JSON).get(String.class);
        T r2 = udpateTestResource(r1);
        r2 = (T) wr.type(MediaType.APPLICATION_XML).put(r2.getClass(), r2);
        String response2 = wr.accept(MediaType.APPLICATION_JSON).get(String.class);
        Assert.assertFalse(response1.equals(response2));
    }
}
