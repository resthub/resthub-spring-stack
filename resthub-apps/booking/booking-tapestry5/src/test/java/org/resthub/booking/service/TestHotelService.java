package org.resthub.booking.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.booking.model.Hotel;
import org.resthub.core.test.service.AbstractResourceServiceTest;

public class TestHotelService extends AbstractResourceServiceTest<Hotel, HotelService> {

	private static final String CHANGED_TEST_HOTEL_NAME = "changedTestHotelName";
	
	private Hotel hotel;

	@Override
	@Inject
	@Named("hotelService")
	public void setResourceService(HotelService hotelService) {
		this.resourceService = hotelService;
	}
	
	@Override
	protected Hotel createTestRessource() throws Exception {
		hotel = new Hotel();
		hotel.setName("testHotelName");
		hotel.setAddress("testHotelAddress");
		hotel.setCity("testHotelCity");
		hotel.setZip("ZIP");
		hotel.setCountry("testHotelCountry");
		return hotel;
	}

	@Override
	public void testUpdate() throws Exception {
		
		hotel = this.resourceService.findById(hotel.getId());
		assertNotNull("hotel should not be null", hotel);
		
		hotel.setName(CHANGED_TEST_HOTEL_NAME);
		hotel = this.resourceService.update(hotel);
		assertEquals("hotel name should have been modified", CHANGED_TEST_HOTEL_NAME, hotel.getName());
	}



}
