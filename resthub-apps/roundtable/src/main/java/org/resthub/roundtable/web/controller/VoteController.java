package org.resthub.roundtable.web.controller;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.resthub.core.service.ResourceGenericService;
import org.resthub.roundtable.domain.model.Vote;
import org.resthub.web.controller.AbstractGenericResourceController;

import com.sun.jersey.api.view.Viewable;

/**
 * Vote controller.
 * @author Nicolas Carlier (mailto:pouicbox@yahoo.fr)
 */
@Path("/vote")
@Named("voteController")
public class VoteController extends AbstractGenericResourceController<Vote> {

    @Inject
    @Named("voteService")
    public void setResourceService(ResourceGenericService<Vote> resourceService) {
        this.resourceService = resourceService;
    }

    @Override
    public void delete(Long id) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Response getResources() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Viewable getResourcesView() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Response update(Long id, Vote resource) {
        throw new UnsupportedOperationException("Not supported.");
    }
}
