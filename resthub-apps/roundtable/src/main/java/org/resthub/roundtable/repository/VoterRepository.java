package org.resthub.roundtable.repository;

import org.resthub.roundtable.model.Poll;
import org.resthub.roundtable.model.Voter;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Voter Repository.
 * 
 * @author Nicolas Carlier
 */
public interface VoterRepository extends JpaRepository<Voter, Long> {

    /**
     * Find Voter by his name and poll
     * 
     * @param name
     *            voter name
     * @param poll
     *            poll
     * @return the voter (null if not found)
     */
    Voter findByNameAndPoll(String name, Poll poll);
}
