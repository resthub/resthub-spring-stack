package org.resthub.booking.dao;

import java.util.List;
import org.apache.lucene.queryParser.ParseException;
import org.resthub.booking.model.Hotel;
import org.resthub.core.dao.GenericResourceDao;


/**
 * Poll DAO.
 * @author Nicolas Carlier
 */
public interface HotelDao extends GenericResourceDao<Hotel>  {
    /**
     * Find hotel by fulltext search.
     * @param query query
	 * @param offset rows sets to ignore. A set contains 'limit' rows.
	 * @param limit max rows number to fetch
     * @return hotels matching query
     * @throws ParseException if bad query syntaxe
     */
    List<Hotel> find(String query, Integer offset, Integer limit) throws ParseException;

    /**
     * Rebuil full index.
     */
    void rebuildIndex();
}
