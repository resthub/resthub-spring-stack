package org.resthub.web.model;

import org.fest.assertions.api.Assertions;
import org.resthub.web.JsonHelper;
import org.testng.annotations.Test;

public class WebSampleResourceSerializationTest {

    @Test
    public void testWebSampleResourceJsonSerialization() {
        WebSampleResource resource = new WebSampleResource("testResource");
        String output = JsonHelper.serialize(resource);
        Assertions.assertThat(output).contains("testResource");
    }

}