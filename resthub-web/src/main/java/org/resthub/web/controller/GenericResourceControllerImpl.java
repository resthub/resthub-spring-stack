package org.resthub.web.controller;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.ws.rs.PathParam;

import org.resthub.core.domain.model.Resource;
import org.resthub.core.service.ResourceService;

public abstract class GenericResourceControllerImpl<T extends Resource> implements GenericResourceController<T> {
	
	protected Class<T> entityClass;
    private T[] entityClassArray; 
    
	protected ResourceService<T> resourceService;
	
	public GenericResourceControllerImpl() {
		Class clazz = getClass();
        Type genericSuperclass = clazz.getGenericSuperclass();
        while (!(genericSuperclass instanceof ParameterizedType)) {
            clazz = clazz.getSuperclass();
            genericSuperclass = clazz.getGenericSuperclass();
        }
        this.entityClass = (Class<T>) ((ParameterizedType) genericSuperclass)
                .getActualTypeArguments()[0];
            
        entityClassArray = (T[]) Array.newInstance(this.entityClass, 0); 
	}
	
	public void setResourceService(ResourceService<T> resourceService) {
		this.resourceService = resourceService;
	}
	
		
	public T create(T resource) {
		return this.resourceService.create(resource);
	}
		
	public T[] findAll() {
		List<T> resources = this.resourceService.findAll();
		return resources.toArray(entityClassArray);
	}
	
	public T[] findByName(@PathParam("name")String name) {
		List<T> resources = this.resourceService.findAll();
		return resources.toArray(entityClassArray);
	}

}
