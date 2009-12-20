package org.resthub.core.domain.dao;

import java.util.List;

public interface ResourceDao<T> {

    public void persist(T transientResource);

    public T merge(T detachedResource);

    public void remove(T persistentResource);

    public void remove(Long resourceId);

    public T findById(Long id);

    public List<T> findAll();

    public T findByName(String name);
    
    public List<T> findAllByLabel(String label);
    
    public List<T> findAllByPartialLabel(String label);
}
