package org.resthub.roundtable.domain.dao.jpa;


import javax.inject.Named;

import org.resthub.core.domain.dao.jpa.GenericJpaResourceDao;
import org.resthub.roundtable.domain.dao.PollDao;
import org.resthub.roundtable.domain.model.Poll;

/**
 * {@inheritDoc}
 */
@Named("pollDao")
public class JpaPollDao extends GenericJpaResourceDao<Poll> implements PollDao {

}
