package org.resthub.booking.web;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.resthub.booking.service.HotelService;

/**
 * Lucene Index controller.
 * @author Nicolas Carlier
 */
@Path("/lucene")
@Named("luceneIndexController")
@Singleton
public class LuceneIndexControler {

    protected HotelService hotelService;

    @Inject
    @Named("hotelService")
    public void setService(HotelService pollService) {
        this.hotelService = pollService;
    }

    @POST
    @Path("/rebuild")
    @Produces(MediaType.TEXT_PLAIN)
    public Response rebuildIndex() {
        this.hotelService.rebuildIndex();
        return Response.ok("index rebuiled").build();
    }
}
