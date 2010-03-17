package org.resthub.roundtable.domain.dao.impl.jpa;

import javax.inject.Named;
import org.resthub.roundtable.domain.dao.AnswerDao;


import org.resthub.roundtable.domain.model.Answer;
import org.resthub.core.domain.dao.jpa.AbstractJpaResourceDao;

/**
 * {@inheritDoc}
 */
@Named("answerDao")
public class AnswerDaoImpl extends AbstractJpaResourceDao<Answer> implements AnswerDao {

}
