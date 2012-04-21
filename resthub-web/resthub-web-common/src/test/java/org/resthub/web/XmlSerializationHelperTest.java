package org.resthub.web;

import org.resthub.web.exception.SerializationException;
import org.resthub.web.model.SampleResource;
import org.testng.Assert;
import org.testng.annotations.Test;

public class XmlSerializationHelperTest {

    private String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><sampleResource><description>desc</description><id>123</id><name>Albert</name></sampleResource>";

    @Test
    public void testSerialization() {
        SampleResource r = new SampleResource();
        r.setId(123L);
        r.setName("Albert");
        r.setDescription("desc");
        String result = XmlHelper.serialize(r);
        Assert.assertTrue(result.contains("<id>123</id>"));
        Assert.assertTrue(result.contains("<name>Albert</name>"));
        Assert.assertTrue(result.contains("<description>desc</description>"));
    }

    @Test
    public void testDeserialization() {
        SampleResource r = (SampleResource) XmlHelper.deserialize(xml, SampleResource.class);
        Assert.assertEquals(new Long(123).longValue(), r.getId().longValue());
        Assert.assertTrue(r.getName().equals("Albert"));
        Assert.assertTrue(r.getDescription().equals("desc"));
    }

    @Test(expectedExceptions=SerializationException.class)
    public void testInvalidDeserialization() {
        XmlHelper.deserialize("Invalid content", SampleResource.class);
    }

}
