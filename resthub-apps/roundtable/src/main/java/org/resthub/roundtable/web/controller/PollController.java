package org.resthub.roundtable.web.controller;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.resthub.core.model.Resource;

import org.resthub.roundtable.model.Poll;
import org.resthub.roundtable.service.PollService;
import org.resthub.roundtable.service.common.ServiceException;
import org.resthub.web.controller.GenericResourceController;
import org.resthub.web.response.PageResponse;
import org.synyx.hades.domain.PageRequest;

/**
 * Poll controller.
 * @author Nicolas Carlier
 */
@Path("/poll")
@Named("pollController")
public class PollController extends GenericResourceController<Poll, PollService> {

    @Inject
    @Named("pollService")
    @Override
    public void setService(PollService service) {
        this.service = service;
    }

    @GET
    @Path("/search")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getResources(
            @QueryParam("q") String q,
            @QueryParam("page") @DefaultValue("0") Integer page,
            @QueryParam("size") @DefaultValue("5") Integer size) {

        PageResponse<Poll> polls;
        try {
            polls = new PageResponse<Poll>(
                    this.service.find(q, new PageRequest(page, size)));
        } catch (ServiceException ex) {
            return Response.serverError().header("ServiceException", ex.getMessage()).build();
        }

        return Response.ok(polls).build();
    }
}
