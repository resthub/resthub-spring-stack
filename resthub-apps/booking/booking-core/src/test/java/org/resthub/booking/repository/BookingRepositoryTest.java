package org.resthub.booking.repository;

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
import org.resthub.core.test.repository.AbstractRepositoryTest;

public class BookingRepositoryTest extends AbstractRepositoryTest<Booking, Long, BookingRepository> {

    private static final String TEST_CARD_NAME = "testCardName";
    private static final String CHANGED_TEST_CARD_NAME = "changedTestCardName";
   
    private User testUser;
    private Booking testBooking;
    
    @Inject
    @Named("userRepository")
    private UserRepository userRepository;

    @Inject
    @Named("hotelRepository")
    private HotelRepository hotelRepository;

    @Override
    @Inject
    @Named("bookingRepository")
    public void setRepository(BookingRepository bookingRepository) {
        this.repository = bookingRepository;
    }

    @Override
    protected Booking createTestEntity() {
        Hotel hotel = new Hotel();
        hotel.setName("testBookingName" + new Random().nextInt(10000));
        hotel.setAddress("testBookingAddress");
        hotel.setCity("testBookingCity");
        hotel.setZip("ZIP");
        hotel.setCountry("testBookingCountry");
        hotel = hotelRepository.save(hotel);

        testUser = new User();
        String username = "user" + new Random().nextInt(10000);
        testUser.setUsername(username);
        testUser.setEmail(Calendar.getInstance().getTimeInMillis() + "test@booking.user");
        testUser.setFullname("testBookingUserFullname");
        testUser.setPassword("password");
        testUser = userRepository.save(testUser);

        testBooking = new Booking();
        testBooking.setHotel(hotel);
        testBooking.setCreditCardName(TEST_CARD_NAME);
        testBooking.setCreditCardNumber("1111111111111111");
        testBooking.setCreditCardType(CreditCardType.VISA);
        testBooking.setCheckinDate(Calendar.getInstance().getTime());
        testBooking.setCheckoutDate(Calendar.getInstance().getTime());
        testBooking.setUser(testUser);
        return testBooking;
    }

    @Override
    @Test
    public void testUpdate() {
        Booking updatedBooking = this.repository.findOne(testBooking.getId());

        updatedBooking.setCreditCardName(CHANGED_TEST_CARD_NAME);
        updatedBooking = this.repository.save(updatedBooking);
        assertEquals("Card name should have been modified", CHANGED_TEST_CARD_NAME, updatedBooking.getCreditCardName());
    }

    @Test
    public void testFindByUser() {
        List<Booking> bookings = this.repository.findByUser(testUser);
        assertEquals("bookings list should contain an unique result", 1, bookings.size());
        assertEquals("credit card names should be equals", TEST_CARD_NAME, bookings.get(0).getCreditCardName());
    }
}
