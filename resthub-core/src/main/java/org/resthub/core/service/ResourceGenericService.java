package org.resthub.core.service;

import org.resthub.core.domain.model.Resource;

/**
 * Generic Resource Service interface.
 * @param <T> Resource class
 */
public interface ResourceGenericService<T extends Resource> extends ResthubGenericService<T, Long> {

}
