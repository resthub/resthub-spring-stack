package org.resthub.roundtable.service.impl;


import javax.inject.Inject;
import javax.inject.Named;
import org.resthub.roundtable.domain.dao.PollDao;
import org.resthub.roundtable.domain.model.Poll;
import org.resthub.roundtable.service.PollService;
import org.resthub.core.service.impl.AbstractResourceServiceImpl;

/**
 * Poll service implementation.
 * @author Nicolas Carlier (mailto:pouicbox@yahoo.fr)
 */
@Named("pollService")
public class PollServiceImpl extends AbstractResourceServiceImpl<Poll, PollDao> implements PollService {

    @Inject
    @Named("pollDao")
    @Override
    public void setResourceDao(PollDao pollDao) {
        this.resourceDao = pollDao;
    }

}
