package org.resthub.core.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.domain.dao.ResourceDao;
import org.resthub.core.domain.model.Resource;
import org.resthub.core.service.ResourceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("resourceService")
@Transactional(readOnly = true)
public class ResourceServiceImpl<T extends Resource, D extends ResourceDao<T>> implements
        ResourceService<T> {

    protected D resourceDao;

    @Inject
    @Named("resourceDao")
    public void setResourceDao(D resourceDao) {
        this.resourceDao = resourceDao;
    }

    @Transactional(readOnly = false)
    public void create(T transientResource) {
        resourceDao.persist(transientResource);
    }

    @Transactional(readOnly = false)
    public T update(T detachedResource) {
        return resourceDao.merge(detachedResource);
    }

    @Transactional(readOnly = false)
    public void delete(T persistentResource) {
        resourceDao.remove(persistentResource);
    }

    @Transactional(readOnly = false)
    public void delete(Long resourceId) {
        resourceDao.remove(resourceId);
    }

    public T findById(Long id) {
        return resourceDao.findById(id);
    }

    public List<T> findAll() {
        return resourceDao.findAll();
    }

    public T findByName(String name) {
        return resourceDao.findByName(name);
    }
    
    public List<T> findAllByLabel(String label) {
        return resourceDao.findAllByLabel(label);
    }
    
    public List<T> findAllByPartialLabel(String label) {
        return resourceDao.findAllByPartialLabel(label);
    }

}
