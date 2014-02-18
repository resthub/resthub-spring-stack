package org.resthub.web.controller;

import org.resthub.common.view.ResponseView;
import org.resthub.web.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller @RequestMapping("/book")
public class BookController {

    List<Book> data = new ArrayList<Book>();

    public BookController()
    {
        data.add(new Book("Effective Java","Joshua Bloch","Essential",1));
        data.add(new Book("Breaking Dawn","Stephanie Myers","Just terrible",2));
    }

    @RequestMapping
    public @ResponseBody List<Book> getBooks()
    {
        return data;
    }
    @RequestMapping(value= "summaries", params="page=no")
    @ResponseView(Book.SummaryView.class)
    public @ResponseBody List<Book> getBookSummaries()
    {
        return data;
    }
    @RequestMapping("summaries")
    @ResponseView(Book.SummaryView.class)
    public @ResponseBody Page<Book> getBookSummariesPaginated(@RequestParam(value = "page", required = true, defaultValue = "1") Integer page)
    {
        return new PageImpl<Book>(data);
    }
    @RequestMapping("{id}/summary")
    @ResponseView(Book.SummaryView.class)
    public @ResponseBody Book getSummary(@PathVariable("id") Integer id)
    {
        return data.get(id - 1);
    }
    @RequestMapping("{id}")
    public @ResponseBody Book getDetail(@PathVariable("id") Integer id)
    {
        return data.get(id - 1);
    }
}