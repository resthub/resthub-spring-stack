package org.resthub.web.controller;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import org.fest.assertions.api.Assertions;
import org.resthub.test.common.AbstractWebTest;
import org.resthub.web.Client;
import org.resthub.web.Http;
import org.resthub.web.model.Sample;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class XmlRepositoryBasedRestControllerWebTest extends AbstractWebTest {

    protected String rootUrl() {
        return "http://localhost:" + port + "/repository-based";
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
    public void testCreateResource() throws IllegalArgumentException, InterruptedException, ExecutionException, IOException {
        Client httpClient = new Client();
        Sample r = new Sample("toto");
        Client.Response response = httpClient.url(rootUrl()).xmlPost(r).get();
        r = (Sample)response.xmlDeserialize(r.getClass());
        Assertions.assertThat(r).isNotNull();
        Assertions.assertThat(r.getName()).isEqualTo("toto");
    }

    @Test
    public void testFindAllResources() throws IllegalArgumentException, InterruptedException, ExecutionException, IOException {
        Client httpClient = new Client();
    	httpClient.url(rootUrl()).xmlPost(new Sample("toto")). get();
        httpClient.url(rootUrl()).xmlPost(new Sample("toto")).get();
    	String responseBody = httpClient.url(rootUrl()).getXml().get().getBody();
        Assertions.assertThat(responseBody).contains("toto");
    }
    
    @Test
    public void testPagingFindAllResources() throws IllegalArgumentException, InterruptedException, ExecutionException, IOException {
        Client httpClient = new Client();
    	httpClient.url(rootUrl()).xmlPost(new Sample("toto")). get();
        httpClient.url(rootUrl()).xmlPost(new Sample("toto")).get();
    	String responseBody = httpClient.url(rootUrl() + "/page/0").getXml().get().getBody();
        Assertions.assertThat(responseBody).contains("<totalElements>2</totalElements>");
    }

    @Test
    public void testDeleteResource() throws IllegalArgumentException, IOException, InterruptedException, ExecutionException {
        Client httpClient = new Client();
    	Sample r = new Sample("toto");
        r = (Sample)httpClient.url(rootUrl()).xmlPost(r).get().xmlDeserialize(r.getClass());
        Assertions.assertThat(r).isNotNull();

        Client.Response response = httpClient.url(rootUrl() + "/" + r.getId()).delete().get();
        Assertions.assertThat(response.getStatus()).isEqualTo(Http.NO_CONTENT);
        
        response = httpClient.url(rootUrl() + "/" + r.getId()).get().get();
        Assertions.assertThat(response.getStatus()).isEqualTo(Http.NOT_FOUND);
    }

    @Test
    public void testFindResource() throws IllegalArgumentException, IOException, InterruptedException, ExecutionException {
        Client httpClient = new Client();
        Sample r = new Sample("toto");
        r = (Sample)httpClient.url(rootUrl()).xmlPost(r).get().xmlDeserialize(r.getClass());
        
        Client.Response response = httpClient.url(rootUrl() + "/" + r.getId()).get().get();
        Assertions.assertThat(response.getStatus()).isEqualTo(Http.OK);
    }

    @Test
    public void testUpdate() throws IllegalArgumentException, IOException, InterruptedException, ExecutionException {
        Client httpClient = new Client();
        Sample r1 = new Sample("toto");
        r1 = httpClient.url(rootUrl()).xmlPost(r1).get().xmlDeserialize(r1.getClass());
        Sample r2 = new Sample(r1);
        r2.setName("titi");
        r2 = httpClient.url(rootUrl() + "/" + r1.getId()).xmlPut(r2).get().xmlDeserialize(r2.getClass());
        Assertions.assertThat(r1).isNotEqualTo(r2);
        Assertions.assertThat(r1.getName()).contains("toto");
        Assertions.assertThat(r2.getName()).contains("titi");
    }

}