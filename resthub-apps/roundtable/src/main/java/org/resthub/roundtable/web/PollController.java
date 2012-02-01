package org.resthub.roundtable.web;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.resthub.roundtable.model.Poll;
import org.resthub.roundtable.service.PollService;
import org.resthub.roundtable.service.common.ServiceException;
import org.resthub.web.controller.GenericControllerImpl;
import org.resthub.web.response.PageResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Poll controller.
 * 
 * @author Nicolas Carlier
 */
@Controller @RequestMapping("/api/poll")
public class PollController extends GenericControllerImpl<Poll, Long, PollService> {

	@Inject
	@Named("pollService")
	@Override
	public void setService(PollService service) {
		this.service = service;
	}

	@GET
	@Path("/search")
	public Response getResources(@QueryParam("q") String q, @QueryParam("page") @DefaultValue("0") Integer page,
			@QueryParam("size") @DefaultValue("5") Integer size) {

		PageResponse<Poll> polls;
		try {
			polls = new PageResponse<Poll>(this.service.find(q, new PageRequest(page, size, Direction.DESC,
					"creationDate")));
		} catch (ServiceException ex) {
			return Response.serverError().header("ServiceException", ex.getMessage()).build();
		}

		return Response.ok(polls).build();
	}
}
