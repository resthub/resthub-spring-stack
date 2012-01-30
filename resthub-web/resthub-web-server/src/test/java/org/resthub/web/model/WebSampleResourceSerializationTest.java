package org.resthub.web.model;

import junit.framework.Assert;

import org.junit.Test;
import org.resthub.web.SerializationHelper;

public class WebSampleResourceSerializationTest {

    @Test
    public void testWebSampleResourceJsonSerialization() {
        WebSampleResource resource = new WebSampleResource("testResource");
        String output = SerializationHelper.jsonSerialize(resource);
        Assert.assertTrue(output.contains("testResource"));
    }

}