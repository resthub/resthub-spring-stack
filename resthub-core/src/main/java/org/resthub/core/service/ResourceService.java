package org.resthub.core.service;

import java.util.List;

public interface ResourceService<T> {
	
	List<T> findAll();
	T findByPath(String path);
	T findByName(String name);
	void create(T resource);	

}
