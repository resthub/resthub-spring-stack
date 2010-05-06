package org.resthub.roundtable.domain.dao;

import org.resthub.core.domain.dao.GenericResourceDao;
import org.resthub.roundtable.domain.model.Poll;
import org.resthub.roundtable.domain.model.Voter;

/**
 * Voter DAO.
 * @author Nicolas Carlier (mailto:pouicbox@yahoo.fr)
 */
public interface VoterDao extends GenericResourceDao<Voter> {

    /**
     * Find Voter by his name and poll
     * @param name voter name
     * @param poll poll
     * @return the voter (null if not found)
     */
    Voter findByNameAndPoll(String name, Poll poll);
}
