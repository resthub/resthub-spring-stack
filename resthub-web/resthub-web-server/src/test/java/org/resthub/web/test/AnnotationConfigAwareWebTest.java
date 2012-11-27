package org.resthub.web.test;

import org.fest.assertions.api.Assertions;
import org.resthub.test.AbstractWebTest;
import org.resthub.web.model.Sample;
import org.testng.annotations.Test;


public class AnnotationConfigAwareWebTest extends AbstractWebTest {
    
    AnnotationConfigAwareWebTest() {
        super("resthub-web-server,resthub-jpa");
    }
    
    @Test
    public void testSample() {
        Sample sample = this.request("annotation-config/test").getJson().resource(Sample.class);
        Assertions.assertThat(sample).isNotNull();
        Assertions.assertThat(sample.getName()).isEqualTo("toto");
                
    }
    
}
