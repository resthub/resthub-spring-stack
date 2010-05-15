/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.resthub.web.controller;

import org.resthub.core.model.Resource;
import org.resthub.core.service.GenericResourceService;

public abstract class GenericResourceController<T extends Resource, S extends GenericResourceService<T>> extends GenericController<T, S, Long> {

    @Override
    public Long generateIdentifierFromEntity(T entity) {
        return entity.getId();
    }


}
