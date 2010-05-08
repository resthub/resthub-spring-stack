package org.resthub.roundtable.dao.jpa;


import javax.inject.Named;

import org.resthub.core.dao.jpa.GenericJpaResourceDao;
import org.resthub.roundtable.dao.PollDao;
import org.resthub.roundtable.model.Poll;

/**
 * {@inheritDoc}
 */
@Named("pollDao")
public class JpaPollDao extends GenericJpaResourceDao<Poll> implements PollDao {

}
