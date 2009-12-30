package org.resthub.core.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.domain.dao.ResourceDao;
import org.resthub.core.domain.model.Resource;
import org.resthub.core.service.ResourceService;
import org.springframework.transaction.annotation.Transactional;

@Named("resourceService")
@Transactional(readOnly = true)
public class ResourceServiceImpl<T extends Resource> implements
        ResourceService<T> {

    protected ResourceDao<T> resourceDao;

    @Inject
    @Named("resourceDao")
    public void setResourceDao(ResourceDao<T> resourceDao) {
        this.resourceDao = resourceDao;
    }

    @Transactional(readOnly = false)
    public T create(T resource) {
        return resourceDao.merge(resource);
    }

    @Transactional(readOnly = false)
    public T update(T resource) {
        return resourceDao.merge(resource);
    }

    @Transactional(readOnly = false)
    public void delete(T resource) {
        resourceDao.remove(resource);
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
