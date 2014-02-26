package org.resthub.web.test;


import org.fest.assertions.api.Assertions;
import org.resthub.test.AbstractWebTest;
import org.resthub.web.Http;
import org.resthub.web.Response;
import org.resthub.web.exception.NotFoundClientException;
import org.resthub.web.model.Sample;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class SluggableRepositoryBasedRestControllerTest extends AbstractWebTest {

     public SluggableRepositoryBasedRestControllerTest() {
        super("resthub-web-server, resthub-jpa, resthub-pool-bonecp");
    }
    
    @AfterMethod
    public void tearDown() {
        this.request("sluggable-repository-based").delete();
    }

    @Test(expectedExceptions = {NotFoundClientException.class})
    public void testDeleteResource() {
        Sample r = new Sample("toto");
        r = this.request("sluggable-repository-based").jsonPost(r).resource(r.getClass());
        Assertions.assertThat(r).isNotNull();

        Response response = this.request("sluggable-repository-based/toto").delete();
        Assertions.assertThat(response.getStatus()).isEqualTo(Http.NO_CONTENT);

        this.request("sluggable-repository-based/toto").get();
    }

    @Test
    public void testFindResource() {
        Sample r = new Sample("toto");
        this.request("sluggable-repository-based").jsonPost(r).resource(r.getClass());

        Response response = this.request("sluggable-repository-based/toto").get();
        Assertions.assertThat(response.getStatus()).isEqualTo(Http.OK);
    }

    @Test
    public void testUpdate() {
        Sample r1 = new Sample("toto");
        r1 = this.request("sluggable-repository-based").jsonPost(r1).resource(r1.getClass());
        Sample r2 = new Sample(r1);
        r2.setName("titi");
        r2 = this.request("sluggable-repository-based/toto").jsonPut(r2).resource(r2.getClass());
        Assertions.assertThat(r1).isNotEqualTo(r2);
        Assertions.assertThat(r1.getName()).contains("toto");
        Assertions.assertThat(r2.getName()).contains("titi");
    }
}
