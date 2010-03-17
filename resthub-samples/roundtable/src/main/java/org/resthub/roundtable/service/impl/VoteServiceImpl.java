package org.resthub.roundtable.service.impl;


import javax.inject.Inject;
import javax.inject.Named;
import org.resthub.roundtable.domain.dao.VoteDao;
import org.resthub.roundtable.domain.model.Vote;
import org.resthub.roundtable.service.VoteService;
import org.resthub.core.service.impl.AbstractResourceServiceImpl;

/**
 * Vote service implementation.
 * @author Nicolas Carlier (mailto:pouicbox@yahoo.fr)
 */
@Named("voteService")
public class VoteServiceImpl extends AbstractResourceServiceImpl<Vote, VoteDao> implements VoteService {

    @Inject
    @Named("voteDao")
    @Override
    public void setResourceDao(VoteDao pollDao) {
        this.resourceDao = pollDao;
    }

}
