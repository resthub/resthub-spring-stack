package org.resthub.booking.web;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.booking.model.Booking;
import org.resthub.booking.service.BookingService;
import org.resthub.web.controller.GenericControllerImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Guillaume Zurbach
 */
@Controller @RequestMapping("/api/booking")
public class BookingController extends GenericControllerImpl<Booking, Long, BookingService> {

    /**
     * {@inheritDoc}
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
    @RequestMapping(method = RequestMethod.GET, value = "user/{id}") @ResponseBody
    public List<Booking> getBookingsByUser(@PathVariable("id") String userId) {
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
