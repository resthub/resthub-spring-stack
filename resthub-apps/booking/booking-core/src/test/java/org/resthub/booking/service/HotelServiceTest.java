package org.resthub.booking.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Assert;
import org.junit.Test;
import org.resthub.booking.model.Hotel;
import org.resthub.core.test.service.AbstractServiceTest;

public class HotelServiceTest extends AbstractServiceTest<Hotel, Long, HotelService> {

    private static final String CHANGED_TEST_HOTEL_NAME = "testHotelName2";
    private static final String CHANGED_TEST_HOTEL_STATE = "testState2";

    private Hotel hotel;

    @Override
    @Inject
    @Named("hotelService")
    public void setService(HotelService hotelService) {
        this.service = hotelService;
    }

    @Override
    protected Hotel createTestEntity() {
        hotel = new Hotel();
        hotel.setName("testHotelName");
        hotel.setAddress("testHotelAddress");
        hotel.setCity("testHotelCity");
        hotel.setState("testState");
        hotel.setZip("ZIP");
        hotel.setCountry("testHotelCountry");
        return hotel;
    }

    @Override
    @Test
    public void testUpdate() {

        hotel = this.service.findById(hotel.getId());
        assertNotNull("hotel should not be null", hotel);

        hotel.setState(CHANGED_TEST_HOTEL_STATE);
        hotel = this.service.update(hotel);
        assertEquals("hotel name should have been modified", CHANGED_TEST_HOTEL_STATE, hotel.getState());
    }

    @Override
    @Test
    public void testCreate() {
        hotel = this.createTestEntity();
        hotel.setName(CHANGED_TEST_HOTEL_NAME);
        hotel = service.create(hotel);
        hotel = service.findById(hotel.getId());
        Assert.assertNotNull("Resource not created!", hotel);
    }
}
