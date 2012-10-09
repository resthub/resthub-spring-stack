package org.resthub.web.controller;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import org.fest.assertions.api.Assertions;
import org.resthub.test.common.AbstractWebTest;
import org.resthub.web.Client;
import org.resthub.web.Http;
import org.resthub.web.Response;
import org.resthub.web.model.Sample;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;


public class JsonRepositoryBasedRestControllerTest extends AbstractWebTest {

    public JsonRepositoryBasedRestControllerTest() {
        this.activeProfiles = "resthub-web-server,resthub-jpa";
    }

    protected String rootUrl() {
        return "http://localhost:" + port + "/service-based";
    }

    @AfterMethod
    public void tearDown() {
        try {
            new Client().url(rootUrl()).delete().get();
        } catch (InterruptedException | ExecutionException e) {
            Assertions.fail("Exception during delete all request", e);
        }
    }

    @Test
    public void testCreateResource() throws IllegalArgumentException, InterruptedException, ExecutionException,
            IOException {
        Sample r = new Sample("toto");
        Response response = new Client().url(rootUrl()).jsonPost(r).get();
        r = (Sample) response.resource(r.getClass());
        Assertions.assertThat(r).isNotNull();
        Assertions.assertThat(r.getName()).isEqualTo("toto");
    }

    @Test
    public void testFindAllResources() throws IllegalArgumentException, InterruptedException, ExecutionException,
            IOException {
        Client httpClient = new Client();
        httpClient.url(rootUrl()).jsonPost(new Sample("toto")).get();
        httpClient.url(rootUrl()).jsonPost(new Sample("toto")).get();
        String responseBody = httpClient.url(rootUrl()).getJson().get().getBody();
        Assertions.assertThat(responseBody).contains("toto");
        Assertions.assertThat(responseBody).contains("\"totalElements\":2");
        Assertions.assertThat(responseBody).contains("\"numberOfElements\":2");
    }
    
    @Test
    public void testFindAllResourcesUnpaginated() throws IllegalArgumentException, InterruptedException, ExecutionException,
            IOException {
        Client httpClient = new Client();
        httpClient.url(rootUrl()).jsonPost(new Sample("toto")).get();
        httpClient.url(rootUrl()).jsonPost(new Sample("toto")).get();
        String responseBody = httpClient.url(rootUrl()).setQueryParameter("page", "no").getJson().get().getBody();
        Assertions.assertThat(responseBody).contains("toto");
        Assertions.assertThat(responseBody).doesNotContain("\"totalElements\":2");
        Assertions.assertThat(responseBody).doesNotContain("\"numberOfElements\":2");
    }

    @Test
    public void testFindPaginatedResources() throws IllegalArgumentException, InterruptedException, ExecutionException, IOException {
        Client httpClient = new Client();
        httpClient.url(rootUrl()).xmlPost(new Sample("toto")).get();
        httpClient.url(rootUrl()).xmlPost(new Sample("toto")).get();
        String responseBody = httpClient.url(rootUrl()).setQueryParameter("page", "1")
                .getJson().get().getBody();
        Assertions.assertThat(responseBody).contains("\"totalElements\":2");
        Assertions.assertThat(responseBody).contains("\"numberOfElements\":2");
        responseBody = httpClient.url(rootUrl()).setQueryParameter("page", "1")
                .setQueryParameter("size", "1").getJson().get().getBody();
        Assertions.assertThat(responseBody).contains("\"totalElements\":2");
        Assertions.assertThat(responseBody).contains("\"numberOfElements\":1");
    }
    
    @Test
    public void testFindPaginatedResourcesReturnsBadRequestForAnInvalidPageNumber() throws InterruptedException, ExecutionException {
        Client httpClient = new Client();
        httpClient.url(rootUrl()).xmlPost(new Sample("toto")).get();
        httpClient.url(rootUrl()).xmlPost(new Sample("toto")).get();
        Response response = httpClient.url(rootUrl()).setQueryParameter("page", "0")
                .getJson().get();
        // TODO: Map IllegalArgumentException to 400 Bad Request
        Assertions.assertThat(response.getStatus()).isEqualTo(500);
        Assertions.assertThat(response.getBody()).contains("Page index must be greater than 0");
    }

    @Test
    public void testDeleteResource() throws IllegalArgumentException, IOException, InterruptedException,
            ExecutionException {
        Client httpClient = new Client();
        Sample r = new Sample("toto");
        r = httpClient.url(rootUrl()).jsonPost(r).get().resource(r.getClass());
        Assertions.assertThat(r).isNotNull();

        Response response = httpClient.url(rootUrl() + "/" + r.getId()).delete().get();
        Assertions.assertThat(response.getStatus()).isEqualTo(Http.NO_CONTENT);
        response = httpClient.url(rootUrl() + "/" + r.getId()).get().get();
        Assertions.assertThat(response.getStatus()).isEqualTo(Http.NOT_FOUND);
    }

    @Test
    public void testFindResource() throws IllegalArgumentException, IOException, InterruptedException,
            ExecutionException {
        Client httpClient = new Client();
        Sample r = new Sample("toto");
        r = httpClient.url(rootUrl()).jsonPost(r).get().resource(r.getClass());

        Response response = httpClient.url(rootUrl() + "/" + r.getId()).get().get();
        Assertions.assertThat(response.getStatus()).isEqualTo(Http.OK);
    }

    @Test
    public void testUpdate() throws IllegalArgumentException, IOException, InterruptedException, ExecutionException {
        Client httpClient = new Client();
        Sample r1 = new Sample("toto");
        r1 = httpClient.url(rootUrl()).jsonPost(r1).get().resource(r1.getClass());
        Sample r2 = new Sample(r1);
        r2.setName("titi");
        r2 = httpClient.url(rootUrl() + "/" + r1.getId()).jsonPut(r2).get().resource(r2.getClass());
        Assertions.assertThat(r1).isNotEqualTo(r2);
        Assertions.assertThat(r1.getName()).contains("toto");
        Assertions.assertThat(r2.getName()).contains("titi");
    }
}
