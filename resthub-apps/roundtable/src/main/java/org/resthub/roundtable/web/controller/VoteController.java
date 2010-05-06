package org.resthub.roundtable.web.controller;

import java.net.URI;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.resthub.roundtable.service.VoteService;

/**
 * Vote controller.
 * @author Nicolas Carlier (mailto:pouicbox@yahoo.fr)
 */
@Path("/vote")
@Named("voteController")
@Singleton
public class VoteController {

    protected VoteService voteService;

    @Context
    private UriInfo uriInfo;

    @Inject
    @Named("voteService")
    public void setResourceService(VoteService voteService) {
        this.voteService = voteService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response vote(
            @FormParam(value="voter") String voter,
            @FormParam(value="pid") Long pid,
            @FormParam(value="values[]") List<String> values) {

        this.voteService.vote(voter, pid, values);

        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
        URI uri = uriBuilder.path(pid.toString()).build();

        return Response.created(uri).entity(pid.toString()).build();
    }
}
