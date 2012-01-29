package org.resthub.web.controller;

import java.io.Serializable;
import java.util.List;

import org.resthub.web.response.PageResponse;

/**
 * <p>
 * Generic REST controller
 * </p>
 * 
 * <p>
 * It provides following generic web services, all usable with XML or JSON serialization :
 * <ul>
 * <li>GET on / : returns paged response of entities managed by this controller
 * <li>GET on /unpaged return all entities managed by this controller</li>
 * <li>GET on /{id} : returns the entity identified by 'id'</li>
 * <li>POST on / : create a new entity (given in parameter)</li>
 * <li>PUT on /{id} : update an entity (given in parameter)</li>
 * <li>DELETE on /{id} : delete the entity identified by 'id'</li>
 * </ul>
 * </p>
 * 
 * </pre>
 * 
 * @author sdeleuze
 * @param <T>
 *            Entity to manage
 * @param <ID>
 *            Entity key
 */
public interface GenericController<T, ID extends Serializable> {

    T create(T entity);

    T update(ID id, T entity);

    List<T> findAll();

    PageResponse<T> findAll(Integer page, Integer size);

    T findById(ID id);

    void delete();

    void delete(ID id);
}