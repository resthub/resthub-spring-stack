package org.resthub.booking.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.After;
import org.junit.Test;
import org.resthub.booking.dao.HotelDao;
import org.resthub.booking.dao.UserDao;
import org.resthub.booking.model.Booking;
import org.resthub.booking.model.CreditCardType;
import org.resthub.booking.model.Hotel;
import org.resthub.booking.model.User;
import org.resthub.core.test.service.AbstractResourceServiceTest;

public class TestBookingService extends AbstractResourceServiceTest<Booking, BookingService> {

	private static final String TEST_CARD_NAME = "testCardName";
	private static final String CHANGED_TEST_CARD_NAME = "changedTestCardName";
	private User user;
	private Booking booking;
	
	@Inject
	@Named("userDao")
	private UserDao userDao;
	
	@Inject
	@Named("hotelDao")
	private HotelDao hotelDao;

	@Override
	@Inject
	@Named("bookingService")
	public void setResourceService(BookingService bookingService) {
		this.resourceService = bookingService;
	}
	
	@Override
	protected Booking createTestRessource() throws Exception {
		Hotel hotel = new Hotel();
		hotel.setName("testBookingName"+new Random().nextInt(10000));
		hotel.setAddress("testBookingAddress");
		hotel.setCity("testBookingCity");
		hotel.setZip("ZIP");
		hotel.setCountry("testBookingCountry");
		hotel = hotelDao.save(hotel);
		
		user = new User ();
		String username = "user"+new Random().nextInt(10000);
		user.setUsername(username);
		user.setEmail(Calendar.getInstance().getTimeInMillis()+"test@booking.user");
		user.setFullname("testBookingUserFullname");
		user.setPassword("password");
		user = userDao.save(user);
		
		booking = new Booking();
		booking.setHotel(hotel);
		booking.setCreditCardName(TEST_CARD_NAME);
		booking.setCreditCardNumber("1111111111111111");
		booking.setCreditCardType(CreditCardType.VISA);
		booking.setCheckinDate(Calendar.getInstance().getTime());
		booking.setCheckoutDate(Calendar.getInstance().getTime());
		booking.setUser(user);
		return booking;
	}
	
	@After
	@Override
    public void tearDown() throws Exception {
    	// Don't use deleteAll because it does not acheive cascade delete
		for (Booking currentBooking : resourceService.findAll()) {
    		resourceService.delete(currentBooking);
        }
    	for (Hotel hotel : hotelDao.readAll()) {
    		hotelDao.delete(hotel);
        }
    	for (User currentUser : userDao.readAll()) {
    		userDao.delete(currentUser);
        }    	
    }

	@Override
	@Test
	public void testUpdate() throws Exception {
		
		booking = this.resourceService.findById(booking.getId());
		assertNotNull("booking should not be null", booking);
		
		booking.setCreditCardName(CHANGED_TEST_CARD_NAME);
		booking = this.resourceService.update(booking);
		assertEquals("Card name should have been modified", CHANGED_TEST_CARD_NAME, booking.getCreditCardName());
	}
	
	@Test
	public void testFindByUser() {
		List<Booking> bookings = this.resourceService.findByUserId(user.getId());
		assertEquals("bookings list should contain an unique result", 1, bookings.size());
		assertEquals("credit card names should be equals", TEST_CARD_NAME, bookings.get(0).getCreditCardName());
	}
}
