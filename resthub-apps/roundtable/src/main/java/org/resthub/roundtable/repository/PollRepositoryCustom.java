package org.resthub.roundtable.repository;

import org.apache.lucene.queryParser.ParseException;
import org.resthub.roundtable.model.Poll;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Guillaume Zurbach
 * @author bmeurant <Baptiste Meurant>
 */
public interface PollRepositoryCustom {

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
