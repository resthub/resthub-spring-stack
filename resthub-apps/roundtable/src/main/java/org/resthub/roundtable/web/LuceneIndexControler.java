package org.resthub.roundtable.web;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.roundtable.service.PollService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Lucene Index controller.
 * 
 * @author Nicolas Carlier
 */
@Controller @RequestMapping("/api/lucene")
public class LuceneIndexControler {

    protected PollService pollService;

    @Inject
    @Named("pollService")
    public void setService(PollService pollService) {
        this.pollService = pollService;
    }


    @RequestMapping(method = RequestMethod.POST, value = "rebuild") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void rebuildIndex() {
        this.pollService.rebuildIndex();
    }
}
