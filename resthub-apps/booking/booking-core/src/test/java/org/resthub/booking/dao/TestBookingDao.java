package org.resthub.booking.dao;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.resthub.booking.model.Booking;
import org.resthub.booking.model.CreditCardType;
import org.resthub.booking.model.Hotel;
import org.resthub.booking.model.User;
import org.resthub.core.test.dao.AbstractResourceDaoTest;

public class TestBookingDao extends AbstractResourceDaoTest<Booking, BookingDao> {

	private static final String TEST_CARD_NAME = "testCardName";
	private static final String CHANGED_TEST_CARD_NAME = "changedTestCardName";
	private User user;
	
	@Inject
	@Named("userDao")
	private UserDao userDao;
	
	@Inject
	@Named("hotelDao")
	private HotelDao hotelDao;

	@Override
	@Inject
	@Named("bookingDao")
	public void setResourceDao(BookingDao bookingDao) {
		this.resourceDao = bookingDao;
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
		
		Booking booking = new Booking();
		booking.setHotel(hotel);
		booking.setCreditCardName(TEST_CARD_NAME);
		booking.setCreditCardNumber("1111111111111111");
		booking.setCreditCardType(CreditCardType.VISA);
		booking.setCheckinDate(Calendar.getInstance().getTime());
		booking.setCheckoutDate(Calendar.getInstance().getTime());
		booking.setUser(user);
		return booking;
	}

	@Override
	@Test
	public void testUpdate() throws Exception {
		
		List<Booking> bookings = this.resourceDao.findEquals("creditCardName", TEST_CARD_NAME);
		assertEquals("bookings list should contain an unique result", 1, bookings.size());
		
		Booking booking = bookings.get(0);
		booking.setCreditCardName(CHANGED_TEST_CARD_NAME);
		booking = this.resourceDao.save(booking);
		assertEquals("Card name should have been modified", CHANGED_TEST_CARD_NAME, booking.getCreditCardName());
	}
	
	@Test
	public void testFindByUser() {
		List<Booking> bookings = this.resourceDao.findByUser(user);
		assertEquals("bookings list should contain an unique result", 1, bookings.size());
		assertEquals("credit card names should be equals", TEST_CARD_NAME, bookings.get(0).getCreditCardName());
	}

}
