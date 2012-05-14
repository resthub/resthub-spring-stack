package org.resthub.web;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import org.springframework.data.domain.Page;

/**
 * Wrapper for Spring Data paged responses, builtin support for XML and JSON serialization
 */
@XmlRootElement
public class PageResponse {
	protected int number;
	protected int size;
	protected int totalPages;
	protected int numberOfElements;
	protected long totalElements;
	protected List<?> elements;

	protected PageResponse() {
		super();
	}

	public PageResponse(Page<?> page) {
		number = page.getNumber();
		size = page.getSize();
		totalPages = page.getTotalPages();
		numberOfElements = page.getNumberOfElements();
		totalElements = page.getTotalElements();
		elements = page.getContent();
	}

	@XmlElementWrapper(name = "elements")
	@XmlElement(name = "element")
	public List<?> getElements() {
		return elements;
	}

	public void setElements(List<?> elements) {
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
