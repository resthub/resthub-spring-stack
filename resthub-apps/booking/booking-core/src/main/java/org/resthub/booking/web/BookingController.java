package org.resthub.booking.web;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.resthub.booking.model.Booking;
import org.resthub.booking.service.BookingService;
import org.resthub.web.controller.GenericResourceController;

/**
 * @author Guillaume Zurbach
 */
@Path("/booking")
@Named("bookingController")
public class BookingController extends GenericResourceController<Booking, BookingService> {

	/**
	 * {@InheritDoc}
	 */
	@Inject
	@Named("bookingService")
	@Override
	public void setService(BookingService service) {
		this.service = service;
	}

	/**
	 * @param userId
	 * 
	 * @return all bookings made by user identified by userId
	 */
	@GET
	@Path("/user/{id}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getBookingsByUser(@PathParam("id") String userId) {
		List<Booking> bookings = null;
		
		Long id = Long.valueOf(userId);
		
		if (id !=null) {
			bookings = this.service.findByUserId(id);
		}
		if (bookings == null) {
			bookings = new ArrayList<Booking>();
		}
		
		return Response.ok(bookings).build();
	}
}
