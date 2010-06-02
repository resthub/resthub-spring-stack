package org.resthub.roundtable.dao;

import java.util.List;
import org.apache.lucene.queryParser.ParseException;
import org.resthub.core.dao.GenericResourceDao;
import org.resthub.roundtable.model.Poll;


/**
 * Poll DAO.
 * @author Nicolas Carlier
 */
public interface PollDao extends GenericResourceDao<Poll>  {
    /**
     * Find poll by fulltext search.
     * @param query query
     * @return polls matching query
     * @throws ParseException if bad query syntaxe
     */
    List<Poll> find(String query) throws ParseException;

    /**
     * Rebuil full index.
     */
    void rebuildIndex();
}
