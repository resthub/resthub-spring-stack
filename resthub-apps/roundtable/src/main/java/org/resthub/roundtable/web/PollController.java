package org.resthub.roundtable.web;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.roundtable.model.Poll;
import org.resthub.roundtable.service.PollService;
import org.resthub.roundtable.service.common.ServiceException;
import org.resthub.web.controller.GenericControllerImpl;
import org.resthub.web.exception.InternalServerErrorException;
import org.resthub.web.response.PageResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
	
	@RequestMapping(method = RequestMethod.GET, value = "search") @ResponseBody
    public PageResponse<Poll> searchHotels(@RequestParam(value="q", required=false) String q,
            								@RequestParam(value="page", required=false) Integer page, 
            								@RequestParam(value="size", required=false) Integer size) {
		
		page = (page == null) ? 0 : page;
        size = (size == null) ? 5 : size;

		PageResponse<Poll> polls;
		try {
			polls = new PageResponse<Poll>(this.service.find(q, new PageRequest(page, size, Direction.DESC,
					"creationDate")));
		} catch (ServiceException e) {
			throw new InternalServerErrorException(e);
		}
		
		return polls;
	}
}