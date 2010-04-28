package org.resthub.roundtable.domain.dao.jpa;

import javax.inject.Named;
import javax.persistence.Query;

import org.resthub.core.domain.dao.jpa.GenericJpaResourceDao;
import org.resthub.roundtable.domain.dao.VoteDao;
import org.resthub.roundtable.domain.model.Poll;
import org.resthub.roundtable.domain.model.Vote;

/**
 * {@inheritDoc}
 */
@Named("voteDao")
public class JpaVoteDao extends GenericJpaResourceDao<Vote> implements VoteDao {

    /**
     * {@inheritDoc}
     */
    public boolean exist(String voter, Poll poll) {
        final Query query = getEntityManager().createNamedQuery("existVote");
        query.setParameter("voter", voter);
        query.setParameter("pid", poll.getId());

        return ((Long) query.getSingleResult() >= 1);
    }
}
