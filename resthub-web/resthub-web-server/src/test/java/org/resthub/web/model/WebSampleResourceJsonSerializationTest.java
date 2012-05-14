package org.resthub.web.model;

import org.fest.assertions.api.Assertions;
import org.resthub.web.JsonHelper;
import org.testng.annotations.Test;

public class WebSampleResourceJsonSerializationTest {

    @Test
    public void testWebSampleResourceJsonSerialization() {
        Sample resource = new Sample("testResource");
        String output = JsonHelper.serialize(resource);
        Assertions.assertThat(output).contains("testResource");
    }

}