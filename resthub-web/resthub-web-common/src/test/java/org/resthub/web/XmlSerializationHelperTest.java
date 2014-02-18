package org.resthub.web;

import org.fest.assertions.api.Assertions;
import org.resthub.web.exception.SerializationException;
import org.resthub.web.model.Book;
import org.resthub.web.model.SampleResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class XmlSerializationHelperTest {

    private String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><sampleResource><description>desc</description><id>123</id><name>Albert</name></sampleResource>";

    @Test
    public void testSerialization() {
        SampleResource r = new SampleResource();
        r.setId(123L);
        r.setName("Albert");
        r.setDescription("desc");
        String result = XmlHelper.serialize(r);
        Assertions.assertThat(result).contains("<id>123</id>");
        Assertions.assertThat(result).contains("<name>Albert</name>");
        Assertions.assertThat(result).contains("<description>desc</description>");
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
        
        String result = XmlHelper.serialize(p);
        Assertions.assertThat(result).contains("Albert");
        
    }

    @Test
    public void testDeserialization() {
        SampleResource r = XmlHelper.deserialize(xml, SampleResource.class);
        Assertions.assertThat(r.getId()).isEqualTo(123);
        Assertions.assertThat(r.getName()).isEqualTo("Albert");
        Assertions.assertThat(r.getDescription()).isEqualTo("desc");
    }

    @Test(expectedExceptions = SerializationException.class)
    public void testInvalidDeserialization() {
        XmlHelper.deserialize("Invalid content", SampleResource.class);
    }

    @Test
    public void testSummaryXml() {
        Book book = new Book("Effective Java","Joshua Bloch","Essential",1);
        String serializedBook = XmlHelper.serialize(book, Book.SummaryView.class);
        Book unserializedBook = XmlHelper.deserialize(serializedBook, Book.class);
        Assertions.assertThat(unserializedBook).isNotNull().isEqualTo(new Book(null,"Joshua Bloch",null,1));
    }

    @Test
    public void testFullXml() {
        Book book = new Book("Effective Java","Joshua Bloch","Essential",1);
        String serializedBook = XmlHelper.serialize(book);
        Book unserializedBook = XmlHelper.deserialize(serializedBook, Book.class);
        Assertions.assertThat(unserializedBook).isNotNull().isEqualTo(new Book("Effective Java","Joshua Bloch","Essential",1));
    }

}
