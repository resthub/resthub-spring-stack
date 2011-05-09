package org.resthub.booking.webapp.t5.components.booking;

import java.util.List;

import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.resthub.booking.model.Booking;
import org.resthub.booking.model.User;
import org.resthub.booking.service.BookingService;
import org.resthub.booking.service.UserService;
import org.resthub.booking.webapp.t5.pages.Search;

/**
 * List all the bookings of the current user. Inspirated by Tapestry5 booking
 * sample (http://tapestry.zones.apache.org:8180/tapestry5-hotel-booking)
 * 
 * @author bmeurant <Baptiste Meurant>
 * @author ccordenier
 */
public class BookingList {

	@Inject
	@Service("bookingService")
	private BookingService bookingService;

	@Inject
	@Service("userService")
	private UserService userService;

	@SuppressWarnings("unused")
	@Parameter(required = false, cache = false)
	@Property
	private Booking current;

	@Property
	private List<Booking> bookings;

	/**
	 * Prepare the list of booking to display, extract all the booking
	 * associated to the current user.
	 * 
	 * @return true if bookings found
	 */
	@SetupRender
	public boolean listBookings() {
		User user = userService.findAll().get(0);
		bookings = bookingService.findByUserId(user.getId());
		return !bookings.isEmpty();
	}

	/**
	 * Simply cancel the booking and redirect to search page
	 */
	@OnEvent(component = "cancelBooking")
	public Object cancelBooking(Long bookingId) {
		bookingService.delete(bookingId);
		return Search.class;
	}

}
