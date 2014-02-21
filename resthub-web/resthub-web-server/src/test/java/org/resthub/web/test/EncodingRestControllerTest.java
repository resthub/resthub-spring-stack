package org.resthub.web.test;

import org.fest.assertions.api.Assertions;
import org.resthub.test.AbstractWebTest;
import org.resthub.web.model.Sample;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;


public class EncodingRestControllerTest extends AbstractWebTest {

    public EncodingRestControllerTest() {
        super("resthub-web-server, resthub-jpa, resthub-pool-bonecp");
    }

    @AfterMethod
    public void tearDown() {
        this.request("repository-based").delete();
    }

    @Test
    public void testEncoding1() {
        Sample r1 = new Sample("Cèdre");
        Sample r2 = this.request("repository-based").jsonPost(r1).resource(r1.getClass());
        Assertions.assertThat(r2.getName()).isEqualTo(r1.getName());
    }

    @Test
    public void testEncoding2() {
        Sample r1 = new Sample("====\u2202====");
        Sample r2 = this.request("repository-based").jsonPost(r1).resource(r1.getClass());
        Assertions.assertThat(r2.getName()).isEqualTo(r1.getName());
    }

    @Test
    public void testEncoding3() {
        Sample r1 = new Sample("====∂====");
        Sample r2 = this.request("repository-based").jsonPost(r1).resource(r1.getClass());
        Assertions.assertThat(r2.getName()).isEqualTo(r1.getName());
    }

}
