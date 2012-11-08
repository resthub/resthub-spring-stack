package org.resthub.web.controller;

import org.fest.assertions.api.Assertions;
import org.resthub.test.AbstractWebTest;
import org.resthub.web.model.Sample;
import org.testng.annotations.Test;


public class AnnotationConfigAwareWebTest extends AbstractWebTest {
    
    AnnotationConfigAwareWebTest() {
        super("resthub-web-server,resthub-jpa");
        this.annotationbasedConfig = true;
        this.contextLocations = "org.resthub.web";
    }
    
    @Test
    public void testSample() {
        Sample sample = this.request("annotation-config/test").getJson().resource(Sample.class);
        Assertions.assertThat(sample).isNotNull();
        Assertions.assertThat(sample.getName()).isEqualTo("toto");
                
    }
    
}
