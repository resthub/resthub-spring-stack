package org.resthub.roundtable.web.controller;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.resthub.core.service.GenericResourceService;
import org.resthub.roundtable.domain.model.Vote;
import org.resthub.roundtable.service.VoteService;
import org.resthub.web.controller.GenericResourceController;

/**
 * Vote controller.
 * @author Nicolas Carlier (mailto:pouicbox@yahoo.fr)
 */
@Path("/vote")
@Named("voteController")
@Singleton
public class VoteController {

    protected VoteService voteService;

    @Inject
    @Named("voteService")
    public void setResourceService(VoteService voteService) {
        this.voteService = voteService;
    }


}
