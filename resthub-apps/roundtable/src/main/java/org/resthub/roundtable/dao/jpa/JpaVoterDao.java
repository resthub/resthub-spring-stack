package org.resthub.roundtable.dao.jpa;

import javax.inject.Named;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.resthub.core.dao.GenericJpaDao;
import org.resthub.roundtable.dao.VoterDao;
import org.resthub.roundtable.model.Poll;
import org.resthub.roundtable.model.Voter;

/**
 * {@inheritDoc}
 */
@Named("voterDao")
public class JpaVoterDao extends GenericJpaDao<Voter, Long> implements VoterDao {

    @Override
    public Voter findByNameAndPoll(String name, Poll poll) {
        final Query query = getEntityManager().createNamedQuery("findVoterByNameAndPoll");
        query.setParameter("name", name);
        query.setParameter("poll", poll);

        try {
            return (Voter) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
