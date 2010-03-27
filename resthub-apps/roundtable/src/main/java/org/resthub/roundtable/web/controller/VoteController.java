package org.resthub.roundtable.web.controller;

import com.sun.jersey.api.view.Viewable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import org.resthub.roundtable.domain.model.Vote;
import org.resthub.core.service.ResourceService;
import org.resthub.web.controller.GenericResourceController;

/**
 * Vote controller.
 * @author Nicolas Carlier (mailto:pouicbox@yahoo.fr)
 */
@Path("/vote")
@Named("voteController")
public class VoteController extends GenericResourceController<Vote> {

    @Inject
    @Named("voteService")
    @Override
    public void setResourceService(ResourceService<Vote> resourceService) {
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
