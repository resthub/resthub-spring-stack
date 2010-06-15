package org.resthub.booking.service;

import java.util.List;
import org.resthub.booking.model.Hotel;
import org.resthub.core.service.GenericResourceService;

/**
 * @author Guillaume Zurbach
 */
public interface HotelService extends GenericResourceService<Hotel> {

	/**
     * Find hotels by full text query.
     * @param query query
	 * @param offset rows sets to ignore. A set contains 'limit' rows.
	 * @param limit max rows number to fetch
     * @return hotels matches
     * @throws ServiceException if bad query
     */
    List<Hotel> find(String query, Integer offset, Integer limit);

	/**
     * Rebuild full index.
     */
    void rebuildIndex();

}
