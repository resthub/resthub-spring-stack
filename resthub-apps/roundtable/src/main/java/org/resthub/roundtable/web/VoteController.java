package org.resthub.roundtable.web;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.roundtable.service.VoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Vote controller.
 * 
 * @author Nicolas Carlier
 */
@Controller @RequestMapping("/api/vote")
public class VoteController {

    protected VoteService voteService;

    @Inject
    @Named("voteService")
    public void setService(VoteService voteService) {
        this.voteService = voteService;
    }

    @RequestMapping(method = RequestMethod.POST, consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody   
    public void vote(@RequestParam(value = "voter") String voter, @RequestParam(value = "pid") Long pid,
    		@RequestParam(value = "values[]") List<String> values) {

        this.voteService.vote(voter, pid, values);
    }
}
