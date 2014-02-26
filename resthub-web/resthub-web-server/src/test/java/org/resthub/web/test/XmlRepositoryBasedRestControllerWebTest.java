package org.resthub.web.test;


import com.fasterxml.jackson.core.type.TypeReference;
import org.fest.assertions.api.Assertions;
import org.resthub.test.AbstractWebTest;
import org.resthub.web.Http;
import org.resthub.web.Response;
import org.resthub.web.exception.BadRequestClientException;
import org.resthub.web.exception.NotFoundClientException;
import org.resthub.web.model.Sample;
import org.springframework.data.domain.Page;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.util.List;

public class XmlRepositoryBasedRestControllerWebTest extends AbstractWebTest {

    public XmlRepositoryBasedRestControllerWebTest() {
         super("resthub-web-server, resthub-jpa, resthub-pool-bonecp");
    }

    @AfterMethod
    public void tearDown() {
        this.request("repository-based").delete();
    }

    @Test
    public void testCreateResource() {
        Sample r = new Sample("toto");
        Response response = this.request("repository-based").xmlPost(r);
        r = response.resource(r.getClass());
        Assertions.assertThat(r).isNotNull();
        Assertions.assertThat(r.getName()).isEqualTo("toto");
    }
    
 @Test
    public void testFindAllResources() {
        this.request("repository-based").xmlPost(new Sample("toto"));
        this.request("repository-based").xmlPost(new Sample("titi"));
        Response response = this.request("repository-based").xmlGet();
        Page<Sample> samples = response.resource(new TypeReference<Page<Sample>>() {});
        Assertions.assertThat(samples).isNotNull();
        Assertions.assertThat(samples.getContent()).isNotNull();
        Assertions.assertThat(samples.getContent().size()).isEqualTo(2);
        Assertions.assertThat(samples.getTotalPages()).isEqualTo(1);
        Assertions.assertThat(samples.getTotalElements()).isEqualTo(2);
        Assertions.assertThat(samples.getContent().get(0).getName()).isIn("titi", "toto");
        Assertions.assertThat(samples.getContent().get(1).getName()).isIn("titi", "toto");
    }

    @Test
    public void testFindAllSortedResources() {
        this.request("repository-based").xmlPost(new Sample("toto"));
        this.request("repository-based").xmlPost(new Sample("titi"));
        Response response = this.request("repository-based").setQueryParameter("direction", "desc").setQueryParameter("properties", "name,id").xmlGet();
        Page<Sample> samples = response.resource(new TypeReference<Page<Sample>>() {});
        Assertions.assertThat(samples).isNotNull();
        Assertions.assertThat(samples.getContent()).isNotNull();
        Assertions.assertThat(samples.getContent().size()).isEqualTo(2);
        Assertions.assertThat(samples.getTotalPages()).isEqualTo(1);
        Assertions.assertThat(samples.getTotalElements()).isEqualTo(2);
        Assertions.assertThat(samples.getContent().get(0).getName()).isIn("titi", "toto");
        Assertions.assertThat(samples.getContent().get(1).getName()).isIn("titi", "toto");
    }

    @Test
    public void testFindAllResourcesUnpaginated() {
        this.request("repository-based").xmlPost(new Sample("toto"));
        this.request("repository-based").xmlPost(new Sample("titi"));
        Response response = this.request("repository-based").setQueryParameter("page", "no").xmlGet();
        List<Sample> samples = response.resource(new TypeReference<List<Sample>>() {});
        Assertions.assertThat(samples).isNotNull();
        Assertions.assertThat(samples.size()).isEqualTo(2);
        Assertions.assertThat(samples.get(0).getName()).isIn("titi", "toto");
        Assertions.assertThat(samples.get(1).getName()).isIn("titi", "toto");
    }


    @Test
    public void testFindPaginatedResources() {
        this.request("repository-based").xmlPost(new Sample("toto"));
        this.request("repository-based").xmlPost(new Sample("titi"));
        Response response = this.request("repository-based").setQueryParameter("page", "1").xmlGet();
        Page<Sample> samples = response.resource(new TypeReference<Page<Sample>>() {});
        Assertions.assertThat(samples).isNotNull();
        Assertions.assertThat(samples.getContent()).isNotNull();
        Assertions.assertThat(samples.getContent().size()).isEqualTo(2);
        Assertions.assertThat(samples.getTotalPages()).isEqualTo(1);
        Assertions.assertThat(samples.getTotalElements()).isEqualTo(2);
        Assertions.assertThat(samples.getContent().get(0).getName()).isIn("titi", "toto");
        Assertions.assertThat(samples.getContent().get(1).getName()).isIn("titi", "toto");
    }

    @Test(expectedExceptions = {BadRequestClientException.class})
    public void testFindPaginatedResourcesReturnsBadRequestForAnInvalidPageNumber() {
        this.request("repository-based").xmlPost(new Sample("toto"));
        this.request("repository-based").xmlPost(new Sample("toto"));
        this.request("repository-based").setQueryParameter("page","0").xmlGet();
    }

    @Test(expectedExceptions = {NotFoundClientException.class})
    public void testDeleteResource() {
        Sample r = new Sample("toto");
        r = this.request("repository-based").xmlPost(r).resource(r.getClass());
        Assertions.assertThat(r).isNotNull();

        Response response = this.request("repository-based/" + r.getId()).delete();
        Assertions.assertThat(response.getStatus()).isEqualTo(Http.NO_CONTENT);

        this.request("repository-based/" + r.getId()).get();
    }

    @Test
    public void testFindResource() {
        Sample r = new Sample("toto");
        r = this.request("repository-based").xmlPost(r).resource(r.getClass());

        Response response = this.request("repository-based/" + r.getId()).get();
        Assertions.assertThat(response.getStatus()).isEqualTo(Http.OK);
    }

    @Test
    public void testFindResources() {
        Sample r = new Sample("toto");
        r = this.request("repository-based").xmlPost(r).resource(r.getClass());

        Sample r2 = new Sample("titi");
        r2 = this.request("repository-based").xmlPost(r2).resource(r2.getClass());

        Response response = this.request("repository-based")
                .setQueryParameter("ids[]", r.getId().toString())
                .setQueryParameter("ids[]", r2.getId().toString()).get();
        Assertions.assertThat(response.getStatus()).isEqualTo(Http.OK);

        List<Sample> samples = response.resource(new TypeReference<Iterable<Sample>>(){});

        Assertions.assertThat(samples).isNotNull();
        Assertions.assertThat(samples).isNotEmpty();
        Assertions.assertThat(samples.size()).isEqualTo(2);
        Assertions.assertThat(samples).contains(r);
        Assertions.assertThat(samples).contains(r2);

        // not found id does not raise error but returns empty list
        response = this.request("repository-based")
                .setQueryParameter("ids[]", "10000000").get();
        Assertions.assertThat(response.getStatus()).isEqualTo(Http.OK);

        samples = response.resource(new TypeReference<Iterable<Sample>>(){});
        Assertions.assertThat(samples).isNotNull();
        Assertions.assertThat(samples).isEmpty();
    }

    @Test
    public void testUpdate() {
        Sample r1 = new Sample("toto");
        r1 = this.request("repository-based").xmlPost(r1).resource(r1.getClass());
        Sample r2 = new Sample(r1);
        r2.setName("titi");
        r2 = this.request("repository-based/" + r1.getId()).xmlPut(r2).resource(r2.getClass());
        Assertions.assertThat(r1).isNotEqualTo(r2);
        Assertions.assertThat(r1.getName()).contains("toto");
        Assertions.assertThat(r2.getName()).contains("titi");
    }

}
