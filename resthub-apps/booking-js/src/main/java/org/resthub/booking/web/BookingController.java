package org.resthub.booking.web;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.resthub.booking.model.Booking;
import org.resthub.booking.service.BookingService;
import org.resthub.web.controller.GenericResourceController;

@Path("/booking")
@Named("bookingController")
public class BookingController extends GenericResourceController<Booking, BookingService> {

	@Inject
	@Named("bookingService")
	@Override
	public void setService(BookingService service) {
		this.service = service;
	}

	/**
	 * Fetch all bookings made by user identified by userId.
	 */
	@GET
	@Path("/user/{id}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getBookingsByUser(@PathParam("id") String userId) {
		List<Booking> bookings = this.service.findByUserId(userId);
		if (bookings == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		
		//return Response.ok(bookings.toArray(entityClassArray)).build();
		return Response.ok(bookings).build();
	}
}
