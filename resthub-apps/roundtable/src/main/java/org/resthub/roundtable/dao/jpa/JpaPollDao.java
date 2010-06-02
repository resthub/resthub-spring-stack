package org.resthub.roundtable.dao.jpa;


import java.util.List;
import javax.inject.Named;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.hibernate.search.jpa.FullTextEntityManager;

import org.resthub.core.dao.GenericJpaResourceDao;
import org.resthub.roundtable.dao.PollDao;
import org.resthub.roundtable.model.Poll;

/**
 * {@inheritDoc}
 */
@Named("pollDao")
public class JpaPollDao extends GenericJpaResourceDao<Poll> implements PollDao {

    @Override
    public List<Poll> find(String query) throws ParseException {
        FullTextEntityManager fullTextEntityManager =
                org.hibernate.search.jpa.Search.getFullTextEntityManager(getEntityManager());
        // create native Lucene query
        String[] fields = new String[]{"author", "topic", "body"};
        MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, new StandardAnalyzer());
        org.apache.lucene.search.Query q = parser.parse(query);
        // wrap Lucene query in a javax.persistence.Query
        javax.persistence.Query persistenceQuery = fullTextEntityManager.createFullTextQuery(q, Poll.class);
        // execute search
        return persistenceQuery.getResultList();
    }
}
