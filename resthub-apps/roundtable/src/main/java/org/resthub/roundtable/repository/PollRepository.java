package org.resthub.roundtable.repository;

import org.resthub.roundtable.model.Poll;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Poll Repository.
 * 
 * @author Nicolas Carlier
 */
public interface PollRepository extends JpaRepository<Poll, Long>, PollRepositoryCustom {
   
}
