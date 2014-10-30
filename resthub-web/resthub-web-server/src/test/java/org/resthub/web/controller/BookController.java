package org.resthub.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.resthub.web.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    List<Book> data = new ArrayList<Book>();

    public BookController() {
        data.add(new Book("Effective Java", "Joshua Bloch", "Essential", 1));
        data.add(new Book("Breaking Dawn", "Stephanie Myers", "Just terrible", 2));
    }

    @RequestMapping
    public List<Book> getBooks() {
        return data;
    }

    @RequestMapping(value = "summaries", params = "page=no")
    @JsonView(Book.SummaryView.class)
    public List<Book> getBookSummaries() {
        return data;
    }

    @RequestMapping("summaries")
    @JsonView(Book.SummaryView.class)
    public Page<Book> getBookSummariesPaginated(@RequestParam(value = "page", required = true, defaultValue = "1") Integer page) {
        return new PageImpl<Book>(data);
    }

    @RequestMapping("{id}/summary")
    @JsonView(Book.SummaryView.class)
    public Book getSummary(@PathVariable("id") Integer id) {
        return data.get(id - 1);
    }

    @RequestMapping("{id}")
    public Book getDetail(@PathVariable("id") Integer id) {
        return data.get(id - 1);
    }
}