package org.resthub.booking.web;

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
import javax.ws.rs.core.Response.Status;
import org.resthub.booking.model.Hotel;
import org.resthub.booking.service.HotelService;
import org.resthub.web.controller.GenericResourceController;
import org.resthub.web.response.PageResponse;
import org.synyx.hades.domain.PageRequest;

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
	 * If query string is empty, fetch all hotels in DB
	 */
	@GET
	@Path("/search")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response searchHotels(
			@QueryParam("q") String query,
			@QueryParam("page") @DefaultValue("0") Integer page,
			@QueryParam("size") @DefaultValue("5") Integer size) {
		
		PageResponse<Hotel> hotels;

		hotels = new PageResponse<Hotel>(
                    this.service.find(query, new PageRequest(page, size)));

		if (hotels == null) {
			return Response.status(Status.BAD_REQUEST).entity("Bad query.").build();
		}
		return Response.ok(hotels).build();
	}
}
