package org.resthub.roundtable.dao;

import org.resthub.core.dao.GenericDao;
import org.resthub.roundtable.model.Poll;
import org.resthub.roundtable.model.Voter;

/**
 * Voter DAO.
 * @author Nicolas Carlier
 */
public interface VoterDao extends GenericDao<Voter, Long> {

    /**
     * Find Voter by his name and poll
     * @param name voter name
     * @param poll poll
     * @return the voter (null if not found)
     */
    Voter findByNameAndPoll(String name, Poll poll);
}
