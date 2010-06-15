package org.resthub.booking.dao;

import java.util.List;
import javax.inject.Named;

import org.resthub.booking.model.Hotel;
import org.resthub.core.dao.GenericJpaResourceDao;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.util.Version;
import org.apache.lucene.search.Query;

import org.hibernate.CacheMode;
import org.hibernate.FlushMode;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.jpa.FullTextEntityManager;

@Named("hotelDao")
public class JpaHotelDao extends GenericJpaResourceDao<Hotel> implements HotelDao {

	private static int BATCH_SIZE = 10;

	@Override
	public List<Hotel> find(String query, Integer offset, Integer limit) throws ParseException {
		FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(getEntityManager());
		// create native Lucene query
		String[] fields = new String[]{"name", "address", "city", "state", "country"};
		MultiFieldQueryParser parser = new MultiFieldQueryParser(Version.LUCENE_29, fields, new StandardAnalyzer(Version.LUCENE_29));
		Query q = parser.parse(query);
		// wrap Lucene query in a javax.persistence.Query
		javax.persistence.Query persistenceQuery = fullTextEntityManager.createFullTextQuery(q, Hotel.class);

		// execute search
		return persistenceQuery.setFirstResult(offset * limit).setMaxResults(limit).getResultList();
	}
	
	@Override
	public void rebuildIndex() {

		FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(getEntityManager());
		
		Session session = (Session) fullTextEntityManager.getDelegate();

		FullTextSession fullTextSession = org.hibernate.search.Search.getFullTextSession(session);

		fullTextSession.setFlushMode(FlushMode.MANUAL);
		fullTextSession.setCacheMode(CacheMode.IGNORE);

		//Scrollable results will avoid loading too many objects in memory
		ScrollableResults results = fullTextSession.createCriteria(Hotel.class).setFetchSize(BATCH_SIZE).scroll(ScrollMode.FORWARD_ONLY);
		int index = 0;
		while (results.next()) {
			index++;
			fullTextSession.index(results.get(0)); //index each element
			if (index % BATCH_SIZE == 0) {
				fullTextSession.flushToIndexes(); //apply changes to indexes
				fullTextSession.clear(); //free memory since the queue is processed
			}
		}
	}
}
