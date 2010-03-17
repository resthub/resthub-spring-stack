package org.resthub.roundtable.domain.dao.impl.jpa;

import javax.inject.Named;
import javax.persistence.Query;

import org.resthub.roundtable.domain.dao.VoteDao;
import org.resthub.roundtable.domain.model.Poll;
import org.resthub.roundtable.domain.model.Vote;
import org.resthub.core.domain.dao.jpa.AbstractJpaResourceDao;

/**
 * {@inheritDoc}
 */
@Named("voteDao")
public class VoteDaoImpl extends AbstractJpaResourceDao<Vote> implements VoteDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean exist(String voter, Poll poll) {
        final Query query = em.createNamedQuery("existVote");
        query.setParameter("voter", voter);
        query.setParameter("pid", poll.getId());

        return ((Long) query.getSingleResult() >= 1) ? true : false;
    }
}
