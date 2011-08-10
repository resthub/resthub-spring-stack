package org.resthub.roundtable.repository;

import org.resthub.roundtable.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Vote Repository.
 * 
 * @author Nicolas Carlier
 */
public interface VoteRepository extends JpaRepository<Vote, Long> {

}
