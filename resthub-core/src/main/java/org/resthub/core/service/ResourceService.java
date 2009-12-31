package org.resthub.core.service;

import java.util.List;

public interface ResourceService<T> {

    public T create(T transientResource);

    public T update(T detachedResource);

    public void delete(T persistentResource);

    public void delete(Long resourceId);

    public T findById(Long id);

    public List<T> findAll();

    public T findByName(String name);
    
}
