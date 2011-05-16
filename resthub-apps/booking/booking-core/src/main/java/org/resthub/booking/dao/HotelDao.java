package org.resthub.booking.dao;

import org.resthub.booking.model.Hotel;
import org.resthub.core.dao.GenericDao;
import org.synyx.hades.domain.Page;
import org.synyx.hades.domain.Pageable;

/**
 * @author Guillaume Zurbach
 * @author bmeurant <Baptiste Meurant>
 */
public interface HotelDao extends GenericDao<Hotel, Long> {

	/**
	 * Find hotel by fulltext search.
	 * 
	 * @param query
	 *            query in HibernateSerach (Lucene) format
	 * @param pageable
	 *            pagination context
	 * @return hotels matching query or null if query string is in an incorrect
	 *         format.
	 */
	Page<Hotel> find(String query, Pageable pageable);

	/**
	 * Rebuil full index (Hibernate Search related).
	 */
	void rebuildIndex();
}
