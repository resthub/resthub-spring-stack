package org.resthub.roundtable.web;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.resthub.roundtable.service.PollService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @POST
    @Path("/rebuild")
    @Produces(MediaType.TEXT_PLAIN)
    public Response rebuildIndex() {
        this.pollService.rebuildIndex();
        return Response.ok("index rebuiled").build();
    }
}
