package org.resthub.web;

import org.resthub.web.exception.SerializationException;
import org.resthub.web.model.SampleResource;
import org.testng.Assert;
import org.testng.annotations.Test;

public class JsonSerializationHelperTest {

    private String json = "{\"id\": 123, \"name\": \"Albert\", \"description\": \"desc\"}";
   
    @Test
    public void testSerialization() {
        SampleResource r = new SampleResource();
        r.setId(123L);
        r.setName("Albert");
        r.setDescription("desc");
        String result = JsonHelper.serialize(r);
        Assert.assertTrue(result.contains("123"));
        Assert.assertTrue(result.contains("Albert"));
        Assert.assertTrue(result.contains("desc"));
    }

    @Test
    public void testDeserialization() {
        SampleResource r = (SampleResource) JsonHelper.deserialize(json, SampleResource.class);
        Assert.assertEquals(new Long(123).longValue(), r.getId().longValue());
        Assert.assertTrue(r.getName().equals("Albert"));
        Assert.assertTrue(r.getDescription().equals("desc"));
    }
    
    @Test(expectedExceptions=SerializationException.class)
    public void testInvalidDeserialization() {
        JsonHelper.deserialize("Invalid content", SampleResource.class);
    }

}
