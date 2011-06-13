package org.resthub.booking.web;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.resthub.booking.model.Booking;
import org.resthub.booking.service.BookingService;
import org.resthub.web.controller.GenericControllerImpl;

/**
 * @author Guillaume Zurbach
 */
@Path("/booking")
@Named("bookingController")
public class BookingController extends GenericControllerImpl<Booking, Long, BookingService> {

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
    public List<Booking> getBookingsByUser(@PathParam("id") String userId) {
        List<Booking> bookings = null;

        Long id = Long.valueOf(userId);

        if (id != null) {
            bookings = this.service.findByUserId(id);
        }
        if (bookings == null) {
            bookings = new ArrayList<Booking>();
        }

        return bookings;
    }
}
