package org.resthub.web.model;

import org.resthub.web.JsonHelper;
import org.testng.Assert;
import org.testng.annotations.Test;

public class WebSampleResourceSerializationTest {

    @Test
    public void testWebSampleResourceJsonSerialization() {
        WebSampleResource resource = new WebSampleResource("testResource");
        String output = JsonHelper.serialize(resource);
        Assert.assertTrue(output.contains("testResource"));
    }

}