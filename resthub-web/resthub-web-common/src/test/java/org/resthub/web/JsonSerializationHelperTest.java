package org.resthub.web;

import org.fest.assertions.api.Assertions;
import org.resthub.web.exception.SerializationException;
import org.resthub.web.model.SampleResource;
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
        Assertions.assertThat(result).contains("123");
        Assertions.assertThat(result).contains("Albert");
        Assertions.assertThat(result).contains("desc");
    }

    @Test
    public void testDeserialization() {
        SampleResource r = (SampleResource) JsonHelper.deserialize(json, SampleResource.class);
        Assertions.assertThat(r.getId()).isEqualTo(123);
        Assertions.assertThat(r.getName()).isEqualTo("Albert");
        Assertions.assertThat(r.getDescription()).isEqualTo("desc");
    }

    @Test(expectedExceptions = SerializationException.class)
    public void testInvalidDeserialization() {
        JsonHelper.deserialize("Invalid content", SampleResource.class);
    }
}
