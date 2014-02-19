package org.resthub.web.model;

import com.fasterxml.jackson.annotation.JsonView;

public class Book {

    @JsonView(SummaryView.class)
    private Integer id;

    private String title;

    @JsonView(SummaryView.class)
    private String author;

    private String review;

    public Book() {

    }

    public Book(String title, String author, String review, Integer id) {
        this.title = title;
        this.author = author;
        this.review = review;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;

        Book book = (Book) o;

        return !(author != null ? !author.equals(book.author) : book.author != null)
                && !(id != null ? !id.equals(book.id) : book.id != null)
                && !(review != null ? !review.equals(book.review) : book.review != null)
                && !(title != null ? !title.equals(book.title) : book.title != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (review != null ? review.hashCode() : 0);
        return result;
    }

    // Sample View
    public static interface SummaryView {}
}
