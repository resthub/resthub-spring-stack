package org.resthub.web;

import junit.framework.Assert;

import org.junit.Test;

public class SerializationHelperTest {

    private String json = "{\"id\": 123, \"name\": \"Albert\", \"description\": \"desc\"}";
    private String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><sampleResource><description>desc</description><id>123</id><name>Albert</name></sampleResource>";

    @Test
    public void testXmlSerialization() {
        SampleResource r = new SampleResource();
        r.setId(123L);
        r.setName("Albert");
        r.setDescription("desc");
        String result = SerializationHelper.xmlSerialize(r);
        Assert.assertTrue(result.contains("<id>123</id>"));
        Assert.assertTrue(result.contains("<name>Albert</name>"));
        Assert.assertTrue(result.contains("<description>desc</description>"));
    }

    @Test
    public void testXmlDeserialization() {
        SampleResource r = (SampleResource) SerializationHelper.xmlDeserialize(xml, SampleResource.class);
        Assert.assertEquals(new Long(123).longValue(), r.getId().longValue());
        Assert.assertTrue(r.getName().equals("Albert"));
        Assert.assertTrue(r.getDescription().equals("desc"));
    }

    @Test
    public void testJsonSerialization() {
        SampleResource r = new SampleResource();
        r.setId(123L);
        r.setName("Albert");
        r.setDescription("desc");
        String result = SerializationHelper.jsonSerialize(r);
        Assert.assertTrue(result.contains("123"));
        Assert.assertTrue(result.contains("Albert"));
        Assert.assertTrue(result.contains("desc"));
    }

    @Test
    public void testJsonDeserialization() {
        SampleResource r = (SampleResource) SerializationHelper.jsonDeserialize(json, SampleResource.class);
        Assert.assertEquals(new Long(123).longValue(), r.getId().longValue());
        Assert.assertTrue(r.getName().equals("Albert"));
        Assert.assertTrue(r.getDescription().equals("desc"));
    }

}
