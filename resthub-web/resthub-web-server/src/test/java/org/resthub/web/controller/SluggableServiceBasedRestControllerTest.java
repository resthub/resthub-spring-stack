package org.resthub.web.controller;


import org.fest.assertions.api.Assertions;
import org.resthub.test.common.AbstractWebTest;
import org.resthub.web.Client;
import org.resthub.web.Http;
import org.resthub.web.Response;
import org.resthub.web.exception.BadRequestClientException;
import org.resthub.web.exception.NotFoundClientException;
import org.resthub.web.model.Sample;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class SluggableServiceBasedRestControllerTest extends AbstractWebTest {

    public SluggableServiceBasedRestControllerTest() {
        this.activeProfiles = "resthub-web-server,resthub-jpa";
    }   

    protected String rootUrl() {
        return "http://localhost:" + port + "/sluggable-service-based";
    }

    @AfterMethod
    public void tearDown() {
            new Client().url(rootUrl()).delete();
    }

    @Test(expectedExceptions = {NotFoundClientException.class})
    public void testDeleteResource() {
        Client httpClient = new Client();
        Sample r = new Sample("toto");
        r = httpClient.url(rootUrl()).jsonPost(r).resource(r.getClass());
        Assertions.assertThat(r).isNotNull();

        Response response = httpClient.url(rootUrl() + "/toto").delete();
        Assertions.assertThat(response.getStatus()).isEqualTo(Http.NO_CONTENT);

        httpClient.url(rootUrl() + "/toto").get();
    }

    @Test
    public void testFindResource() {
        Client httpClient = new Client();
        Sample r = new Sample("toto");
        r = httpClient.url(rootUrl()).jsonPost(r).resource(r.getClass());

        Response response = httpClient.url(rootUrl() + "/toto").get();
        Assertions.assertThat(response.getStatus()).isEqualTo(Http.OK);
    }

    @Test
    public void testUpdate() {
        Client httpClient = new Client();
        Sample r1 = new Sample("toto");
        r1 = httpClient.url(rootUrl()).jsonPost(r1).resource(r1.getClass());
        Sample r2 = new Sample(r1);
        r2.setName("titi");
        r2 = httpClient.url(rootUrl() + "/toto").jsonPut(r2).resource(r2.getClass());
        Assertions.assertThat(r1).isNotEqualTo(r2);
        Assertions.assertThat(r1.getName()).contains("toto");
        Assertions.assertThat(r2.getName()).contains("titi");
    }
}
