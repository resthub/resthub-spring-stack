package org.resthub.booking.service;

import java.util.List;

import org.resthub.booking.model.Hotel;
import org.resthub.core.service.GenericResourceService;
import org.synyx.hades.domain.Page;
import org.synyx.hades.domain.Pageable;

/**
 * @author Guillaume Zurbach
 * @author bmeurant <Baptiste Meurant>
 */
public interface HotelService extends GenericResourceService<Hotel> {

	/**
	 * Find hotels by full text query.
	 * 
	 * @param query
	 *            query in HibernateSerach (Lucene) format
	 * @param pageable
	 *            pagination context. ignored if null (all results will be
	 *            returned)
	 * @return hotels matching query or null if query string is in an incorrect
	 *         format.
	 */
	Page<Hotel> find(final String query, final Pageable pageable);

	/**
	 * Find hotels by full text query.
	 * 
	 * @param query
	 *            in HibernateSerach (Lucene) format query
	 * @return hotels matching query or null if query string is in an incorrect
	 *         format.
	 */
	List<Hotel> find(final String query);

	/**
	 * Rebuild full index (Hibernate Search related).
	 */
	void rebuildIndex();

}
