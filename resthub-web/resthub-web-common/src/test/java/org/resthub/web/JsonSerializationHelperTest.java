package org.resthub.web;

import com.fasterxml.jackson.core.type.TypeReference;
import org.fest.assertions.api.Assertions;
import org.resthub.web.exception.SerializationException;
import org.resthub.web.model.Book;
import org.resthub.web.model.SampleResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

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
        SampleResource r = JsonHelper.deserialize(json, SampleResource.class);
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

    @Test
    public void testFullListJson() {
        List<Book> books = new ArrayList<Book>();
        books.add(new Book("Effective Java","Joshua Bloch","Essential",1));
        books.add(new Book("Breaking Dawn","Stephanie Myers","Just terrible",2));
        String serializedBooks = JsonHelper.serialize(books);
        List<Book> unserializedBooks = JsonHelper.deserialize(serializedBooks, new TypeReference<List<Book>>() {});
        Assertions.assertThat(unserializedBooks).isNotNull();
        Assertions.assertThat(unserializedBooks.size()).isEqualTo(2);
        Assertions.assertThat(unserializedBooks).contains(new Book("Effective Java","Joshua Bloch","Essential",1));
        Assertions.assertThat(unserializedBooks).contains(new Book("Breaking Dawn","Stephanie Myers","Just terrible",2));
    }

    @Test
    public void testSummaryListJson() {
        List<Book> books = new ArrayList<Book>();
        books.add(new Book("Effective Java","Joshua Bloch","Essential",1));
        books.add(new Book("Breaking Dawn","Stephanie Myers","Just terrible",2));
        String serializedBooks = JsonHelper.serialize(books, Book.SummaryView.class);
        List<Book> unserializedBooks = JsonHelper.deserialize(serializedBooks, new TypeReference<List<Book>>() {});
        Assertions.assertThat(unserializedBooks).isNotNull();
        Assertions.assertThat(unserializedBooks.size()).isEqualTo(2);
        Assertions.assertThat(unserializedBooks).contains(new Book(null,"Joshua Bloch",null,1));
        Assertions.assertThat(unserializedBooks).contains(new Book(null,"Stephanie Myers",null,2));
    }

    @Test
    public void testSummaryJson() {
        Book book = new Book("Effective Java","Joshua Bloch","Essential",1);
        String serializedBook = JsonHelper.serialize(book, Book.SummaryView.class);
        Book unserializedBook = JsonHelper.deserialize(serializedBook, Book.class);
        Assertions.assertThat(unserializedBook).isNotNull().isEqualTo(new Book(null,"Joshua Bloch",null,1));
    }

    @Test
    public void testFullJson() {
        Book book = new Book("Effective Java","Joshua Bloch","Essential",1);
        String serializedBook = JsonHelper.serialize(book);
        Book unserializedBook = JsonHelper.deserialize(serializedBook, Book.class);
        Assertions.assertThat(unserializedBook).isNotNull().isEqualTo(new Book("Effective Java","Joshua Bloch","Essential",1));
    }


}
