package org.resthub.roundtable.domain.dao.impl.jpa;


import javax.inject.Named;
import org.resthub.roundtable.domain.dao.PollDao;

import org.resthub.roundtable.domain.model.Poll;
import org.resthub.core.domain.dao.jpa.AbstractJpaResourceDao;

/**
 * {@inheritDoc}
 */
@Named("pollDao")
public class PollDaoImpl extends AbstractJpaResourceDao<Poll> implements PollDao {

}
