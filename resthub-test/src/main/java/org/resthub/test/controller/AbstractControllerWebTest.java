package org.resthub.test.controller;

import com.ning.http.client.Response;
import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.ExecutionException;
import org.fest.assertions.api.Assertions;
import org.resthub.test.common.AbstractWebTest;
import org.resthub.web.Http;
import org.resthub.web.JsonHelper;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

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
    @AfterTest
    public void tearDown() {
    	try {
            prepareDelete(getResourcePath() + "/all").execute().get();
            } catch (InterruptedException | ExecutionException | IOException e) {
                Assertions.fail("Exception during delete all request", e);
            }
    }

    @Test
    public void testCreateResource() throws IllegalArgumentException, InterruptedException, ExecutionException, IOException {
        T r = createTestResource();
        Response response = preparePost(getResourcePath()).setBody(JsonHelper.serialize(r)).execute().get();
        r = (T)JsonHelper.deserialize(response.getResponseBody(), r.getClass());
        Assertions.assertThat(r).isNotNull();
    }

    @Test
    public void testFindAllResources() throws IllegalArgumentException, InterruptedException, ExecutionException, IOException {
    	preparePost(getResourcePath()).setBody(JsonHelper.serialize(createTestResource())).execute().get();
    	preparePost(getResourcePath()).setBody(JsonHelper.serialize(createTestResource())).execute().get();
    	String responseBody = prepareGet(getResourcePath()).execute().get().getResponseBody();
        Assertions.assertThat(responseBody).contains("\"totalElements\":2");
    }

    @Test
    public void testDeleteResource() throws IllegalArgumentException, IOException, InterruptedException, ExecutionException {
    	T r = createTestResource();
    	String responseBody = preparePost(getResourcePath()).setBody(JsonHelper.serialize(r)).execute().get().getResponseBody();
    	r = (T)JsonHelper.deserialize(responseBody, r.getClass());
        Assertions.assertThat(r).isNotNull();

        Response response = prepareDelete(getResourcePath() + "/" + getResourceId(r)).execute().get();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(Http.NO_CONTENT);
        
        response = prepareGet(getResourcePath() + "/" + getResourceId(r)).execute().get();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(Http.NOT_FOUND);
    }

    @Test
    public void testFindResource() throws IllegalArgumentException, IOException, InterruptedException, ExecutionException {
        T r = createTestResource();
        String responseBody = preparePost(getResourcePath()).setBody(JsonHelper.serialize(r)).execute().get().getResponseBody();
        r = (T)JsonHelper.deserialize(responseBody, r.getClass());
        
        Response response = prepareGet(getResourcePath() + "/" + getResourceId(r)).execute().get();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(Http.OK);
    }

    @Test
    public void testUpdate() throws IllegalArgumentException, IOException, InterruptedException, ExecutionException {
        T r1 = createTestResource();
        String responseBody1 = preparePost(getResourcePath()).setBody(JsonHelper.serialize(r1)).execute().get().getResponseBody();
        T r2 = udpateTestResource(r1);
        String responseBody2 = preparePut(getResourcePath()).setBody(JsonHelper.serialize(r2)).execute().get().getResponseBody();
        Assertions.assertThat(responseBody1).isNotEqualTo(responseBody2);
    }

}
