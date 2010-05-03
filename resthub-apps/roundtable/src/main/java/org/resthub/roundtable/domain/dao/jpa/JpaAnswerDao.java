package org.resthub.roundtable.domain.dao.jpa;

import javax.inject.Named;

import org.resthub.core.domain.dao.jpa.GenericJpaResourceDao;
import org.resthub.roundtable.domain.dao.AnswerDao;


import org.resthub.roundtable.domain.model.Answer;

/**
 * {@inheritDoc}
 */
@Named("answerDao")
public class JpaAnswerDao extends GenericJpaResourceDao<Answer> implements AnswerDao {

}
