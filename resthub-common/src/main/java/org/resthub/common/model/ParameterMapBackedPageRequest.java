/**
 * 
 */
package org.resthub.common.model;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

/**
 * An implementation of PageRequest that holds all request parameters as a Map
 * provided by 
 * {@link http://static.springsource.org/spring/docs/3.2.x/javadoc-api/org/springframework/web/context/request/WebRequest.html#getParameterMap()}
 */
public class ParameterMapBackedPageRequest extends PageRequest {

	private static final long serialVersionUID = -752023352147402776L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ParameterMapBackedPageRequest.class);

	private Map<String, String[]> parameterMap = null;

	/**
	 * 
	 * @param parameterMap
	 * @param page
	 * @param size
	 */
	public ParameterMapBackedPageRequest(Map<String, String[]> parameterMap, int page, int size) {
		super(page, size);
		this.parameterMap = parameterMap;
	}

	/**
	 * 
	 * @param parameterMap
	 * @param page
	 * @param size
	 * @param sort
	 */
	public ParameterMapBackedPageRequest(Map<String, String[]> parameterMap, int page, int size, Sort sort) {
		super(page, size, sort);
		this.parameterMap = parameterMap;
	}

	/**
	 * 
	 * @param parameterMap
	 * @param page
	 * @param size
	 * @param direction
	 * @param properties
	 */
	public ParameterMapBackedPageRequest(Map<String, String[]> parameterMap, int page, int size, Direction direction, String... properties) {
		super(page, size, direction, properties);
		this.parameterMap = parameterMap;
	}

	public Map<String, String[]> getParameterMap() {
		return parameterMap;
	}

	public void setParameterMap(Map<String, String[]> parameterMap) {
		this.parameterMap = parameterMap;
	}
}
