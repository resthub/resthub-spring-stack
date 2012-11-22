package org.resthub.web.test;

import org.fest.assertions.api.Assertions;
import org.resthub.web.JsonHelper;
import org.resthub.web.model.Sample;
import org.testng.annotations.Test;

public class WebSampleResourceJsonSerializationTest {

    @Test
    public void testWebSampleResourceJsonSerialization() {
        Sample resource = new Sample("testResource");
        String output = JsonHelper.serialize(resource);
        Assertions.assertThat(output).contains("testResource");
    }

}