package org.resthub.booking.dao;

import org.resthub.booking.model.Hotel;
import org.resthub.core.dao.GenericResourceDao;
import org.synyx.hades.domain.Page;
import org.synyx.hades.domain.Pageable;


/**
 * Poll DAO.
 * @author Nicolas Carlier
 */
public interface HotelDao extends GenericResourceDao<Hotel>  {

	/**
     * Find hotel by fulltext search.
     * @param query query
	 * @param pageable
     * @return hotels matching query or null if query string is in an incorrect format.
     */
    Page<Hotel> find(String query, Pageable pageable);

    /**
     * Rebuil full index.
     */
    void rebuildIndex();
}
