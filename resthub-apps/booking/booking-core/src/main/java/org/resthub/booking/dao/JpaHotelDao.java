package org.resthub.booking.dao;

import javax.inject.Named;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import org.hibernate.CacheMode;
import org.hibernate.FlushMode;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.resthub.booking.model.Hotel;
import org.resthub.core.dao.GenericJpaResourceDao;
import org.synyx.hades.domain.Page;
import org.synyx.hades.domain.PageImpl;
import org.synyx.hades.domain.Pageable;

/**
 * @author Guillaume Zurbach
 */
@Named("hotelDao")
public class JpaHotelDao extends GenericJpaResourceDao<Hotel> implements
		HotelDao {

	private static final int BATCH_SIZE = 10;

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Page<Hotel> find(final String query, final Pageable pageable) {
		FullTextEntityManager fullTextEntityManager = Search
				.getFullTextEntityManager(getEntityManager());
		// create native Lucene query
		String[] fields = new String[] { "name", "address", "city", "state",
				"country" };
		MultiFieldQueryParser parser = new MultiFieldQueryParser(
				Version.LUCENE_31, fields, new StandardAnalyzer(
						Version.LUCENE_31));

		Query q;
		try {
			q = parser.parse(query);
		} catch (ParseException ex) {
			return null;
		}

		FullTextQuery persistenceQuery = fullTextEntityManager
				.createFullTextQuery(q, Hotel.class);

		if (pageable == null) {
		    return new PageImpl<Hotel>(persistenceQuery.getResultList());
		} else {
			persistenceQuery.setFirstResult(pageable.getFirstItem());
            persistenceQuery.setMaxResults(pageable.getPageSize());
            return new PageImpl<Hotel>(persistenceQuery.getResultList(),
                    pageable, persistenceQuery.getResultSize());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rebuildIndex() {

		FullTextEntityManager fullTextEntityManager = Search
				.getFullTextEntityManager(getEntityManager());

		Session session = (Session) fullTextEntityManager.getDelegate();

		FullTextSession fullTextSession = org.hibernate.search.Search
				.getFullTextSession(session);

		fullTextSession.setFlushMode(FlushMode.MANUAL);
		fullTextSession.setCacheMode(CacheMode.IGNORE);

		// Scrollable results will avoid loading too many objects in memory
		ScrollableResults results = fullTextSession.createCriteria(Hotel.class)
				.setFetchSize(BATCH_SIZE).scroll(ScrollMode.FORWARD_ONLY);
		int index = 0;
		while (results.next()) {
			index++;
			fullTextSession.index(results.get(0)); // index each element
			if (index % BATCH_SIZE == 0) {
				fullTextSession.flushToIndexes(); // apply changes to indexes
				fullTextSession.clear(); // free memory since the queue is
											// processed
			}
		}
	}
}
