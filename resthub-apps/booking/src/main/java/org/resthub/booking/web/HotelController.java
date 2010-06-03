package org.resthub.booking.web;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.resthub.booking.model.Hotel;
import org.resthub.booking.service.HotelService;
import org.resthub.web.controller.GenericResourceController;

@Path("/hotel")
@Named("hotelController")
public class HotelController extends GenericResourceController<Hotel, HotelService> {

	@Inject
	@Named("hotelService")
	@Override
	public void setService(HotelService service) {
		this.service = service;
	}

	/**
	 * Fetch all hotels containing the value given in parameter
	 */
	@GET
	@Path("/search")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response searchHotels(@QueryParam("q") String query) {

		List<Hotel> hotels = this.service.find(query);
		if (hotels.isEmpty()) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok(hotels).build();
		//return Response.ok(hotels.toArray(entityClassArray)).build();
	}
}
