package org.resthub.web;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Iterator;
import java.util.List;

/**
 * Implementation of Page compatible with Jackson deserialization (default constructor + setters)
 */
@XmlRootElement
public class PageResponse<T extends Object> implements Page<T> {
    
    protected int number;
    protected int size;
    protected int totalPages;
    protected int numberOfElements;
    protected long totalElements;
    protected List<T> content;

    protected PageResponse() {
        super();
    }

    @Override
    @XmlElementWrapper(name = "content")
    @XmlElement(name = "content")
    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    @Override
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    @Override
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    @Override
    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    @Override
    @JsonIgnore
    public boolean hasPreviousPage() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    @JsonIgnore
    public boolean isFirstPage() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    @JsonIgnore
    public boolean hasNextPage() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    @JsonIgnore
    public boolean isLastPage() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    @JsonIgnore
    public Iterator iterator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    @JsonIgnore
    public boolean hasContent() {
        return (content != null) && (!content.isEmpty());
    }

    @Override
    @JsonIgnore
    public Sort getSort() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
