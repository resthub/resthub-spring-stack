/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.resthub.web.controller;

import org.resthub.core.model.Resource;

public abstract class GenericResourceController<T extends Resource> extends GenericController<T, Long> {

    @Override
    public Long generateIdentifierFromEntity(T entity) {
        return entity.getId();
    }


}
