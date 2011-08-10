package org.resthub.booking.service;

import java.util.List;

import org.resthub.booking.model.Hotel;
import org.resthub.core.service.GenericService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Guillaume Zurbach
 * @author bmeurant <Baptiste Meurant>
 */
public interface HotelService extends GenericService<Hotel, Long> {

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
	 * Returns a {@link Page} of Hotels meeting the paging restriction
	 * 
	 * @param pageable
	 * @return a page of Hotels
	 */
	Page<Hotel> findAll(Pageable pageable);

	/**
	 * Rebuild full index (Hibernate Search related).
	 */
	void rebuildIndex();
}
