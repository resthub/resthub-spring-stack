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
 * Provide access to indexation functions from HibernateSearch
 * 
 * @author Nicolas Carlier
 */
@Path("/lucene")
@Named("luceneIndexController")
@Singleton
public class LuceneIndexControler {

	private HotelService hotelService;

	@Inject
	@Named("hotelService")
	public void setService(HotelService hotelService) {
		this.hotelService = hotelService;
	}

	/**
	 * rebuild the HibernateSearch index
	 * 
	 * @return ok response or error code
	 */
	@POST
	@Path("/rebuild")
	@Produces(MediaType.TEXT_PLAIN)
	public Response rebuildIndex() {
		this.hotelService.rebuildIndex();
		return Response.ok("Search engine index rebuilded.").build();
	}
}
