package org.resthub.core.dao;

import java.io.Serializable;
import java.util.List;

/**
 * Generic Dao that can manage any kind of entities. Extends <a href=
 * "http://hades.synyx.org/static/2.x/site/org.synyx.hades/apidocs/org/synyx/hades/dao/GenericDao.html"
 * >Hades GenericDao</a>.
 * 
 * @see <a
 *      href="http://hades.synyx.org/static/2.x/site/org.synyx.hades/apidocs/"
 *      target="_blank">Hades 2.0 Javadoc</a>
 */
public interface GenericDao<T, ID extends Serializable> extends org.synyx.hades.dao.GenericDao<T, ID> {

    /**
     * Delete persisted entity by id.
     * 
     * @param id
     *            entity ID to delete
     */
    void delete(ID id);

    /**
     * Get all Resources (in scrollable resulset).
     * 
     * @param offset
     * @param limit
     * @return list of Resources.
     */
    List<T> readAll(Integer offset, Integer limit);

    /**
     * Retrieves entities whose value corresponding to propertyName parameter
     * matches approximately with the value of propertyValue parameter.
     * 
     * @param propertyName
     *            property name
     * @param propertyValue
     *            property value (e.g. %jean%)
     * @return a list of entities.
     */
    List<T> findLike(String propertyName, String propertyValue);

    /**
     * Retrieves entities whose value corresponding to propertyName parameter
     * matches exactly with the value of propertyValue parameter.
     * 
     * @param propertyName
     *            property name
     * @param propertyValue
     *            property value (e.g. jean)
     * @return a list of entities.
     */
    List<T> findEquals(String propertyName, String propertyValue);
}
