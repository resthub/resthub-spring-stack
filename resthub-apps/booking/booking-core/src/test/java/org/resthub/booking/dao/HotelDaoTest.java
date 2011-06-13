package org.resthub.booking.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.resthub.booking.model.Hotel;
import org.resthub.core.test.dao.AbstractDaoTest;

public class HotelDaoTest extends AbstractDaoTest<Hotel, Long, HotelDao> {

    private static final String CHANGED_TEST_HOTEL_STATE = "hotelState";
    private static final String TEST_HOTEL_NAME = "testHotelName";

    @Override
    @Inject
    @Named("hotelDao")
    public void setDao(HotelDao hotelDao) {
        this.dao = hotelDao;
    }

    @Override
    protected Hotel createTestEntity() {
        Hotel hotel = new Hotel();
        hotel.setName(TEST_HOTEL_NAME + new Random().nextInt(10000));
        hotel.setAddress("testHotelAddress");
        hotel.setCity("testHotelCity");
        hotel.setZip("ZIP");
        hotel.setCountry("testHotelCountry");
        return hotel;
    }

    @Override
    @Test
    public void testUpdate() {

        List<Hotel> hotels = this.dao.findLike("name", TEST_HOTEL_NAME + "%");
        assertEquals("hotels list should contain an unique result", 1, hotels.size());

        Hotel hotel = hotels.get(0);
        hotel.setState(CHANGED_TEST_HOTEL_STATE);
        hotel = this.dao.save(hotel);
        assertEquals("hotel name should have been modified", CHANGED_TEST_HOTEL_STATE, hotel.getState());
    }

}
