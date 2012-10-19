package org.resthub.web;

import java.util.ArrayList;
import java.util.List;
import org.fest.assertions.api.Assertions;
import org.resthub.web.exception.SerializationException;
import org.resthub.web.model.SampleResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    
    @Test
    public void testPageSerialization() {
        SampleResource r1 = new SampleResource();
        r1.setId(123L);
        r1.setName("Albert");
        r1.setDescription("desc");
        
        SampleResource r2 = new SampleResource();
        r2.setId(123L);
        r2.setName("Albert");
        r2.setDescription("desc");
        
        List<SampleResource> l = new ArrayList<SampleResource>();
        l.add(r1);
        l.add(r2);
        
        Page<SampleResource> p = new PageImpl<SampleResource>(l);
        
        String result = JsonHelper.serialize(p);
        Assertions.assertThat(result).contains("Albert");
        
    }

}
