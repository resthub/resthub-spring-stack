package org.resthub.roundtable.repository;

import org.resthub.roundtable.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Answer DAO.
 * 
 * @author Nicolas Carlier
 */
public interface AnswerRepository extends JpaRepository<Answer, Long> {
	
}
