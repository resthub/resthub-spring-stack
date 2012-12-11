package org.resthub.web.test;

import com.fasterxml.jackson.core.type.TypeReference;
import org.fest.assertions.api.Assertions;
import org.resthub.test.AbstractWebTest;
import org.resthub.web.model.Book;
import org.testng.annotations.Test;

import java.util.List;

public class ResponseViewTest extends AbstractWebTest {

    public ResponseViewTest() {
        super("resthub-web-server,resthub-jpa");
    }

    @Test
    public void testFullList() {
        List<Book> books = this.request("book").getJson().resource(new TypeReference<List<Book>>() {
        });
        Assertions.assertThat(books).isNotNull();
        Assertions.assertThat(books.size()).isEqualTo(2);
        Assertions.assertThat(books).contains(new Book("Effective Java","Joshua Bloch","Essential",1));
        Assertions.assertThat(books).contains(new Book("Breaking Dawn","Stephanie Myers","Just terrible",2));
    }

    @Test
    public void testSummaryList() {
        List<Book> books = this.request("book/summaries").getJson().resource(new TypeReference<List<Book>>() {
        });
        Assertions.assertThat(books).isNotNull();
        Assertions.assertThat(books.size()).isEqualTo(2);
        Assertions.assertThat(books).contains(new Book(null,"Joshua Bloch",null,1));
        Assertions.assertThat(books).contains(new Book(null,"Stephanie Myers",null,2));
    }


}
