package org.resthub.roundtable.dao.jpa;

import javax.inject.Named;
import javax.persistence.Query;

import org.resthub.core.dao.GenericJpaResourceDao;
import org.resthub.roundtable.dao.VoteDao;
import org.resthub.roundtable.model.Poll;
import org.resthub.roundtable.model.Vote;

/**
 * {@inheritDoc}
 */
@Named("voteDao")
public class JpaVoteDao extends GenericJpaResourceDao<Vote> implements VoteDao {

    /**
     * {@inheritDoc}
     */
    public boolean exists(String voter, Poll poll) {
        final Query query = getEntityManager().createNamedQuery("existsVote");
        query.setParameter("voter", voter);
        query.setParameter("pid", poll.getId());

        return ((Long) query.getSingleResult() >= 1);
    }
}
