package org.resthub.booking.repository;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.resthub.booking.model.Hotel;
import org.resthub.core.test.repository.AbstractRepositoryTest;

public class HotelRepositoryTest extends AbstractRepositoryTest<Hotel, Long, HotelRepository> {

    private static final String CHANGED_TEST_HOTEL_STATE = "hotelState";
    private static final String TEST_HOTEL_NAME = "testHotelName";

    private Hotel testHotel;
    
    @Override
    @Inject
    @Named("hotelRepository")
    public void setRepository(HotelRepository hotelRepository) {
        this.repository = hotelRepository;
    }

    @Override
    protected Hotel createTestEntity() {
    	testHotel = new Hotel();
        testHotel.setName(TEST_HOTEL_NAME + new Random().nextInt(10000));
        testHotel.setAddress("testHotelAddress");
        testHotel.setCity("testHotelCity");
        testHotel.setZip("ZIP");
        testHotel.setCountry("testHotelCountry");
        return testHotel;
    }

    @Override
    @Test
    public void testUpdate() {
        Hotel updatedHotel = this.repository.findOne(testHotel.getId());

        updatedHotel.setState(CHANGED_TEST_HOTEL_STATE);
        updatedHotel = this.repository.save(testHotel);
        assertEquals("hotel name should have been modified", CHANGED_TEST_HOTEL_STATE, updatedHotel.getState());
    }

	@Override
	public Long getIdFromEntity(Hotel hotel) {
		return hotel.getId();
	}
}
