package org.resthub.roundtable.dao.jpa;

import javax.inject.Named;

import org.resthub.core.dao.GenericJpaResourceDao;
import org.resthub.roundtable.dao.AnswerDao;
import org.resthub.roundtable.model.Answer;

/**
 * {@inheritDoc}
 */
@Named("answerDao")
public class JpaAnswerDao extends GenericJpaResourceDao<Answer> implements AnswerDao {

}
