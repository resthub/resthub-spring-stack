package org.resthub.web.model;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import junit.framework.Assert;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonProcessingException;
import org.junit.Test;
import org.resthub.web.SerializationHelper;

public class WebSampleResourceSerializationTest {

    @Test
    public void testWebSampleResourceJsonSerialization() throws JsonGenerationException, JsonProcessingException,
            IOException {
        WebSampleResource resource = new WebSampleResource("testResource");
        String output = SerializationHelper.jsonSerialize(resource);
        Assert.assertTrue(output.contains("testResource"));
    }

    @Test
    public void testWebSampleResourceXmlSerialization() throws JAXBException {
        WebSampleResource resource = new WebSampleResource("testResource");
        String output = SerializationHelper.xmlSerialize(resource);
        Assert.assertTrue(output.contains("testResource"));
    }
}