package org.resthub.roundtable.domain.dao;

import org.resthub.roundtable.domain.model.Poll;
import org.resthub.roundtable.domain.model.Vote;
import org.resthub.core.domain.dao.ResourceDao;

/**
 * Vote DAO.
 * @author Nicolas Carlier (mailto:pouicbox@yahoo.fr)
 */
public interface VoteDao extends ResourceDao<Vote> {

    /**
     * Test if a vote exist already for a poll.
     * @param voter voter
     * @param poll the poll
     * @return <code>true</code> if a vote already exist, <code>false</code> otherwise
     */
    boolean exist(String voter, Poll poll);
}
