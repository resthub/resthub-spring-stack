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

public class JsonServiceBasedRestControllerTest extends AbstractWebTest {

    public JsonServiceBasedRestControllerTest() {
        super("resthub-web-server,resthub-jpa");
    }

    @AfterMethod
    public void tearDown() {
        this.request("service-based").delete();
    }

    @Test
    public void testCreateResource() {
        Sample r = new Sample("toto");
        Response response = this.request("service-based").jsonPost(r);
        r = (Sample) response.resource(r.getClass());
        Assertions.assertThat(r).isNotNull();
        Assertions.assertThat(r.getName()).isEqualTo("toto");
    }

    @Test
    public void testFindAllResources() {
        this.request("service-based").jsonPost(new Sample("toto"));
        this.request("service-based").jsonPost(new Sample("toto"));
        String responseBody = this.request("service-based").getJson().getBody();
        Assertions.assertThat(responseBody).contains("toto");
        Assertions.assertThat(responseBody).contains("\"totalElements\":2");
        Assertions.assertThat(responseBody).contains("\"numberOfElements\":2");
    }
    
    @Test
    public void testFindAllResourcesUnpaginated() {
        this.request("service-based").jsonPost(new Sample("toto"));
        this.request("service-based").jsonPost(new Sample("toto"));
        String responseBody = this.request("service-based").setQueryParameter("page", "no").getJson().getBody();
        Assertions.assertThat(responseBody).contains("toto");
        Assertions.assertThat(responseBody).doesNotContain("\"totalElements\":2");
        Assertions.assertThat(responseBody).doesNotContain("\"numberOfElements\":2");
    }

    @Test
    public void testFindPaginatedResources() {
        this.request("service-based").xmlPost(new Sample("toto"));
        this.request("service-based").xmlPost(new Sample("toto"));
        String responseBody = this.request("service-based").setQueryParameter("page", "1")
                .getJson().getBody();
        Assertions.assertThat(responseBody).contains("\"totalElements\":2");
        Assertions.assertThat(responseBody).contains("\"numberOfElements\":2");
        responseBody = this.request("service-based").setQueryParameter("page", "1")
                .setQueryParameter("size", "1").getJson().getBody();
        Assertions.assertThat(responseBody).contains("\"totalElements\":2");
        Assertions.assertThat(responseBody).contains("\"numberOfElements\":1");
    }

    @Test(expectedExceptions = {BadRequestClientException.class})
    public void testFindPaginatedResourcesReturnsBadRequestForAnInvalidPageNumber() {
        this.request("service-based").xmlPost(new Sample("toto"));
        this.request("service-based").xmlPost(new Sample("toto"));
        this.request("service-based").setQueryParameter("page", "0").getJson();
    }

    @Test(expectedExceptions = {NotFoundClientException.class})
    public void testDeleteResource() {
        Sample r = new Sample("toto");
        r = this.request("service-based").jsonPost(r).resource(r.getClass());
        Assertions.assertThat(r).isNotNull();

        Response response = this.request("service-based/" + r.getId()).delete();
        Assertions.assertThat(response.getStatus()).isEqualTo(Http.NO_CONTENT);

        this.request("service-based/" + r.getId()).get();
    }

    @Test
    public void testFindResource() {
        Sample r = new Sample("toto");
        r = this.request("service-based").jsonPost(r).resource(r.getClass());

        Response response = this.request("service-based/" + r.getId()).get();
        Assertions.assertThat(response.getStatus()).isEqualTo(Http.OK);
    }

    @Test
    public void testUpdate() {
        Sample r1 = new Sample("toto");
        r1 = this.request("service-based").jsonPost(r1).resource(r1.getClass());
        Sample r2 = new Sample(r1);
        r2.setName("titi");
        r2 = this.request("service-based/" + r1.getId()).jsonPut(r2).resource(r2.getClass());
        Assertions.assertThat(r1).isNotEqualTo(r2);
        Assertions.assertThat(r1.getName()).contains("toto");
        Assertions.assertThat(r2.getName()).contains("titi");
    }
}
