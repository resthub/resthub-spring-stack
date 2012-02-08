package org.resthub.web.test.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.ExecutionException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Test;
import org.resthub.web.Http;
import org.resthub.web.JsonHelper;
import org.resthub.web.test.AbstractWebTest;

import com.ning.http.client.Response;

/**
 * Base class for your generic controller integration tests Run an embeded
 * servlet container in order to test your controller
 */
@SuppressWarnings("unchecked")
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
     * @throws IOException 
     * @throws ExecutionException 
     * @throws InterruptedException 
     */
    @After
    @Override
    public void tearDown() {
    	try {
    		prepareDelete(getResourcePath() + "/all").execute().get();
		} catch (InterruptedException | ExecutionException | IOException e) {
			e.printStackTrace();
		}
        super.tearDown();
    }

    @Test
    public void testCreateResource() throws IllegalArgumentException, InterruptedException, ExecutionException, IOException {
        T r = createTestResource();
        Response response = preparePost(getResourcePath()).setBody(JsonHelper.serialize(r)).execute().get();
        r = (T)JsonHelper.deserialize(response.getResponseBody(), r.getClass());
        Assert.assertNotNull("Resource not created", r);
    }

    @Test
    public void testFindAllResources() throws IllegalArgumentException, InterruptedException, ExecutionException, IOException {
    	preparePost(getResourcePath()).setBody(JsonHelper.serialize(createTestResource())).execute().get();
    	preparePost(getResourcePath()).setBody(JsonHelper.serialize(createTestResource())).execute().get();
    	String responseBody = prepareGet(getResourcePath()).execute().get().getResponseBody();
        Assert.assertTrue("Unable to find all resources or bad-formed JSON", responseBody.contains("\"totalElements\":2"));
    }

    @Test
    public void testDeleteResource() throws IllegalArgumentException, IOException, InterruptedException, ExecutionException {
    	T r = createTestResource();
    	String responseBody = preparePost(getResourcePath()).setBody(JsonHelper.serialize(r)).execute().get().getResponseBody();
    	r = (T)JsonHelper.deserialize(responseBody, r.getClass());
        Assert.assertNotNull("Resource not created", r);

        Response response = prepareDelete(getResourcePath() + "/" + getResourceId(r)).execute().get();
        Assert.assertEquals(Http.NO_CONTENT, response.getStatusCode());
        
        response = prepareGet(getResourcePath() + "/" + getResourceId(r)).execute().get();
        Assert.assertEquals(Http.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testFindResource() throws IllegalArgumentException, IOException, InterruptedException, ExecutionException {
        T r = createTestResource();
        String responseBody = preparePost(getResourcePath()).setBody(JsonHelper.serialize(r)).execute().get().getResponseBody();
        r = (T)JsonHelper.deserialize(responseBody, r.getClass());
        
        Response response = prepareGet(getResourcePath() + "/" + getResourceId(r)).execute().get();
        Assert.assertEquals("Unable to find resource", Http.OK, response.getStatusCode());
    }

    @Test
    public void testUpdate() throws IllegalArgumentException, IOException, InterruptedException, ExecutionException {
        T r1 = createTestResource();
        String responseBody1 = preparePost(getResourcePath()).setBody(JsonHelper.serialize(r1)).execute().get().getResponseBody();
        T r2 = udpateTestResource(r1);
        String responseBody2 = preparePut(getResourcePath()).setBody(JsonHelper.serialize(r2)).execute().get().getResponseBody();
        Assert.assertFalse(responseBody1.equals(responseBody2));
    }

	}
