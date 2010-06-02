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
     * @return hotels matching query
     * @throws ParseException if bad query syntaxe
     */
    List<Hotel> find(String query) throws ParseException;

    /**
     * Rebuil full index.
     */
    void rebuildIndex();
}
