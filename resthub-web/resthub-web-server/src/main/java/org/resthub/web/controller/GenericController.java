package org.resthub.web.controller;

import java.io.Serializable;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.resthub.web.response.PageResponse;

/**
 * <p>
 * Generic REST controller
 * </p>
 * 
 * <p>
 * It provides following generic web services with JSON
 * serialization :
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

    T update(@PathParam("id") ID id, T entity);

    List<T> findAll();

    PageResponse<T> findAll(@QueryParam("page") @DefaultValue("0") Integer page,
            @QueryParam("size") @DefaultValue("5") Integer size);

    T findById(@PathParam("id") ID id);

    void delete();

    void delete(@PathParam("id") ID id);

}