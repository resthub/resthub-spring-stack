package org.resthub.booking.repository;

import org.resthub.booking.model.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Guillaume Zurbach
 * @author bmeurant <Baptiste Meurant>
 */
public interface HotelRepositoryCustom {

	/**
	 * Find hotel by fulltext search.
	 * 
	 * @param query
	 *            query in HibernateSerach (Lucene) format
	 * @param pageable
	 *            pagination context
	 * @return hotels matching query or null if query string is in an incorrect format.
	 */
	Page<Hotel> find(String query, Pageable pageable);

	/**
	 * Rebuil full index (Hibernate Search related).
	 */
	void rebuildIndex();
}
