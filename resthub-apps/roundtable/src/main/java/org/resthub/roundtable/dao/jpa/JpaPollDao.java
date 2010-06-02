package org.resthub.roundtable.dao.jpa;


import java.util.List;
import javax.inject.Named;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.util.Version;
import org.hibernate.CacheMode;
import org.hibernate.FlushMode;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.jpa.FullTextEntityManager;

import org.resthub.core.dao.GenericJpaResourceDao;
import org.resthub.roundtable.dao.PollDao;
import org.resthub.roundtable.model.Poll;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * {@inheritDoc}
 */
@Named("pollDao")
public class JpaPollDao extends GenericJpaResourceDao<Poll> implements PollDao {
    private static int BATCH_SIZE = 10;

    @Override
    public List<Poll> find(String query) throws ParseException {
        FullTextEntityManager fullTextEntityManager =
                org.hibernate.search.jpa.Search.getFullTextEntityManager(getEntityManager());
        // create native Lucene query
        String[] fields = new String[]{"author", "topic", "body"};
        MultiFieldQueryParser parser = new MultiFieldQueryParser(Version.LUCENE_29, fields, new StandardAnalyzer(Version.LUCENE_29));
        org.apache.lucene.search.Query q = parser.parse(query);
        // wrap Lucene query in a javax.persistence.Query
        javax.persistence.Query persistenceQuery = fullTextEntityManager.createFullTextQuery(q, Poll.class);
        // execute search
        return persistenceQuery.getResultList();
    }

    @Override
    public void rebuildIndex() {
        FullTextEntityManager fullTextEntityManager =
                org.hibernate.search.jpa.Search.getFullTextEntityManager(getEntityManager());

        Session session = (Session) fullTextEntityManager.getDelegate();

        FullTextSession fullTextSession = Search.getFullTextSession(session);

        fullTextSession.setFlushMode(FlushMode.MANUAL);
        fullTextSession.setCacheMode(CacheMode.IGNORE);
        //Scrollable results will avoid loading too many objects in memory
        ScrollableResults results = fullTextSession.createCriteria(Poll.class)
            .setFetchSize(BATCH_SIZE)
            .scroll(ScrollMode.FORWARD_ONLY);
        int index = 0;
        while( results.next() ) {
            index++;
            fullTextSession.index(results.get(0)); //index each element
            if (index % BATCH_SIZE == 0) {
                fullTextSession.flushToIndexes(); //apply changes to indexes
                fullTextSession.clear(); //free memory since the queue is processed
            }
        }
    }


}
