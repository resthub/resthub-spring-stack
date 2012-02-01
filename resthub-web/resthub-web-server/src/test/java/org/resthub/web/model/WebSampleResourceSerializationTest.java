package org.resthub.web.model;

import junit.framework.Assert;

import org.junit.Test;
import org.resthub.web.JsonHelper;

public class WebSampleResourceSerializationTest {

    @Test
    public void testWebSampleResourceJsonSerialization() {
        WebSampleResource resource = new WebSampleResource("testResource");
        String output = JsonHelper.serialize(resource);
        Assert.assertTrue(output.contains("testResource"));
    }

}