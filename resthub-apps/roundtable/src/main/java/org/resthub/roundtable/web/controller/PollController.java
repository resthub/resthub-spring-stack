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

import org.resthub.roundtable.model.Poll;
import org.resthub.roundtable.service.PollService;
import org.resthub.roundtable.service.common.ServiceException;
import org.resthub.web.controller.GenericResourceController;
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

        List<Poll> entitys;
        try {
            entitys = this.service.find(q, new PageRequest(page, size)).asList();
        } catch (ServiceException ex) {
            return Response.serverError().build();
        }
        if (entitys.size() == 0) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(entitys.toArray(entityClassArray)).build();
    }
}
