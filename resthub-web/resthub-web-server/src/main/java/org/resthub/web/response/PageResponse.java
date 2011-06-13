package org.resthub.web.response;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.synyx.hades.domain.Page;

/**
 * Wrapper for paged responses.
 * 
 * @author Nicolas Carlier
 */
@XmlRootElement
public class PageResponse<T> {
    protected int number;
    protected int size;
    protected int totalPages;
    protected int numberOfElements;
    protected long totalElements;
    protected List<T> elements;

    protected PageResponse() {
        super();
    }

    public PageResponse(Page<T> page) {
        number = page.getNumber();
        size = page.getSize();
        totalPages = page.getTotalPages();
        numberOfElements = page.getNumberOfElements();
        totalElements = page.getTotalElements();
        elements = page.asList();
    }

    // @XmlJavaTypeAdapter(value=GenericTypeAdapter.class)
    @XmlElementWrapper(name = "elements")
    @XmlElement(name = "element")
    public List<T> getElements() {
        return elements;
    }

    public void setElements(List<T> elements) {
        this.elements = elements;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
