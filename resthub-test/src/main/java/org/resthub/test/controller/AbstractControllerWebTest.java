package org.resthub.test.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.ExecutionException;
import org.fest.assertions.api.Assertions;
import org.resthub.test.common.AbstractWebTest;
import org.resthub.web.Client;
import org.resthub.web.Client.Response;
import org.resthub.web.Http;
import org.resthub.web.JsonHelper;
import org.testng.annotations.AfterMethod;
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
    @AfterMethod
    public void tearDown() {
    	try {
            Client.url("http://localhost:" + port + getResourcePath() + "/all").delete().get();
        } catch (InterruptedException | ExecutionException e) {
            Assertions.fail("Exception during delete all request", e);
        }
    }

    @Test
    public void testCreateResource() throws IllegalArgumentException, InterruptedException, ExecutionException, IOException {
        T r = createTestResource();
        Response response = Client.url("http://localhost:" + port + getResourcePath()).jsonPost(r).get();
        r = (T)response.jsonDeserialize(r.getClass());
        Assertions.assertThat(r).isNotNull();
    }

    @Test
    public void testFindAllResources() throws IllegalArgumentException, InterruptedException, ExecutionException, IOException {
    	Client.url("http://localhost:" + port + getResourcePath()).jsonPost(createTestResource()). get();
        Client.url("http://localhost:" + port + getResourcePath()).jsonPost(createTestResource()).get();
    	String responseBody = Client.url("http://localhost:" + port + getResourcePath() + "/paged").getJson().get().getBody();
        Assertions.assertThat(responseBody).contains("\"totalElements\":2");
    }

    @Test
    public void testDeleteResource() throws IllegalArgumentException, IOException, InterruptedException, ExecutionException {
    	T r = createTestResource();
        r = (T)Client.url("http://localhost:" + port + getResourcePath()).jsonPost(r).get().jsonDeserialize(r.getClass());
        Assertions.assertThat(r).isNotNull();

        Response response = Client.url("http://localhost:" + port + getResourcePath() + "/" + getResourceId(r)).delete().get();
        Assertions.assertThat(response.getStatus()).isEqualTo(Http.NO_CONTENT);
        
        response = Client.url("http://localhost:" + port + getResourcePath() + "/" + getResourceId(r)).get().get();
        Assertions.assertThat(response.getStatus()).isEqualTo(Http.NOT_FOUND);
    }

    @Test
    public void testFindResource() throws IllegalArgumentException, IOException, InterruptedException, ExecutionException {
        T r = createTestResource();
        r = (T)Client.url("http://localhost:" + port + getResourcePath()).jsonPost(r).get().jsonDeserialize(r.getClass());
        
        Response response = Client.url("http://localhost:" + port + getResourcePath() + "/" + getResourceId(r)).get().get();
        Assertions.assertThat(response.getStatus()).isEqualTo(Http.OK);
    }

    @Test
    public void testUpdate() throws IllegalArgumentException, IOException, InterruptedException, ExecutionException {
        T r1 = createTestResource();
        String responseBody1 = Client.url("http://localhost:" + port + getResourcePath()).jsonPost(r1).get().getBody();
        T r2 = udpateTestResource(r1);
        String responseBody2 = Client.url("http://localhost:" + port + getResourcePath()).jsonPut(r2).get().getBody();
        Assertions.assertThat(responseBody1).isNotEqualTo(responseBody2);
    }

}
