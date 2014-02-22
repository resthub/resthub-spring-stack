package org.resthub.web.test;

import com.fasterxml.jackson.core.type.TypeReference;
import org.fest.assertions.api.Assertions;
import org.resthub.test.AbstractWebTest;
import org.resthub.web.PageResponse;
import org.resthub.web.model.Book;
import org.testng.annotations.Test;

import java.util.List;

public class ResponseViewTest extends AbstractWebTest {

    public ResponseViewTest() {
        super("resthub-web-server, resthub-jpa, resthub-pool-bonecp");
    }

    @Test
    public void testFullListJson() {
        List<Book> books = this.request("book").jsonGet().resource(new TypeReference<List<Book>>() {});
        Assertions.assertThat(books).isNotNull();
        Assertions.assertThat(books.size()).isEqualTo(2);
        Assertions.assertThat(books).contains(new Book("Effective Java","Joshua Bloch","Essential",1));
        Assertions.assertThat(books).contains(new Book("Breaking Dawn","Stephanie Myers","Just terrible",2));
    }

    @Test
    public void testSummaryListJson() {
        List<Book> books = this.request("book/summaries").setQueryParameter("page","no").jsonGet().resource(new TypeReference<List<Book>>() {
        });
        Assertions.assertThat(books).isNotNull();
        Assertions.assertThat(books.size()).isEqualTo(2);
        Assertions.assertThat(books).contains(new Book(null,"Joshua Bloch",null,1));
        Assertions.assertThat(books).contains(new Book(null,"Stephanie Myers",null,2));
    }

    @Test
    public void testSummaryPageableListJson() {
        PageResponse<Book> books = this.request("book/summaries").setQueryParameter("page","1").jsonGet().resource(new TypeReference<PageResponse<Book>>() {
        });
        Assertions.assertThat(books).isNotNull();
        Assertions.assertThat(books.getContent().size()).isEqualTo(2);
        Assertions.assertThat(books.getContent()).contains(new Book(null,"Joshua Bloch",null,1));
        Assertions.assertThat(books.getContent()).contains(new Book(null,"Stephanie Myers",null,2));
    }

    @Test
    public void testSummaryJson() {
        Book book = this.request("book/1/summary").jsonGet().resource(Book.class);
        Assertions.assertThat(book).isNotNull().isEqualTo(new Book(null,"Joshua Bloch",null,1));
    }

    @Test
    public void testFullJson() {
        Book book = this.request("book/1").jsonGet().resource(Book.class);
        Assertions.assertThat(book).isNotNull().isEqualTo(new Book("Effective Java","Joshua Bloch","Essential",1));
    }

    @Test
    public void testSummaryXml() {
        Book book = this.request("book/1/summary").xmlGet().resource(Book.class);
        Assertions.assertThat(book).isNotNull().isEqualTo(new Book(null,"Joshua Bloch",null,1));
    }

    @Test
    public void testFullXml() {
        Book book = this.request("book/1").xmlGet().resource(Book.class);
        Assertions.assertThat(book).isNotNull().isEqualTo(new Book("Effective Java","Joshua Bloch","Essential",1));
    }


}
