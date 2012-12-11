package org.resthub.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.resthub.common.view.ResponseView;
import org.resthub.web.model.Book;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @RequestMapping("summaries")
    @ResponseView(Book.SummaryView.class)
    public @ResponseBody List<Book> getBookSummaries()
    {
        return data;
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