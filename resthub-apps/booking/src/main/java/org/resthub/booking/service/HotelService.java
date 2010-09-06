package org.resthub.booking.service;

import org.resthub.booking.model.Hotel;
import org.resthub.core.service.GenericResourceService;
import org.synyx.hades.domain.Page;
import org.synyx.hades.domain.Pageable;

/**
 * @author Guillaume Zurbach
 */
public interface HotelService extends GenericResourceService<Hotel> {

	/**
	 * Find hotels by full text query.
	 * @param query query
	 * @param pageable
	 * @return hotels matching query or null if query string is in an incorrect format.
	 */
    public Page<Hotel> find(final String query, final Pageable pageable);

	/**
     * Rebuild full index.
     */
    void rebuildIndex();

}
