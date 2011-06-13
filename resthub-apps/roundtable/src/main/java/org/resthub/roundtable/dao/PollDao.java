package org.resthub.roundtable.dao;

import org.apache.lucene.queryParser.ParseException;
import org.resthub.core.dao.GenericDao;
import org.resthub.roundtable.model.Poll;
import org.synyx.hades.domain.Page;
import org.synyx.hades.domain.Pageable;

/**
 * Poll DAO.
 * 
 * @author Nicolas Carlier
 */
public interface PollDao extends GenericDao<Poll, Long> {
    /**
     * Find poll by fulltext search.
     * 
     * @param query
     *            query
     * @return polls matching query
     * @throws ParseException
     *             if bad query syntaxe
     */
    Page<Poll> find(String query, Pageable pageable) throws ParseException;

    /**
     * Rebuil full index.
     */
    void rebuildIndex();
}
