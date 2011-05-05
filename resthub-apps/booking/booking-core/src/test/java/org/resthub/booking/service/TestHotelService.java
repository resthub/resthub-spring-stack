package org.resthub.booking.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Assert;
import org.junit.Test;
import org.resthub.booking.model.Hotel;
import org.resthub.core.test.service.AbstractResourceServiceTest;

public class TestHotelService extends AbstractResourceServiceTest<Hotel, HotelService> {

	private static final String CHANGED_TEST_HOTEL_NAME = "testHotelName2";
	private static final String CHANGED_TEST_HOTEL_STATE = "testState2";
	
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
		hotel.setState("testState");
		hotel.setZip("ZIP");
		hotel.setCountry("testHotelCountry");
		return hotel;
	}

	@Override
	@Test
	public void testUpdate() throws Exception {
		
		hotel = this.resourceService.findById(hotel.getId());
		assertNotNull("hotel should not be null", hotel);
		
		hotel.setState(CHANGED_TEST_HOTEL_STATE);
		hotel = this.resourceService.update(hotel);
		assertEquals("hotel name should have been modified", CHANGED_TEST_HOTEL_STATE, hotel.getState());
	}
	
	@Override
	@Test
	public void testCreate() throws Exception {
		hotel = this.createTestRessource();
		hotel.setName(CHANGED_TEST_HOTEL_NAME);
		hotel = resourceService.create(hotel);
		hotel = resourceService.findById(hotel.getId());
		Assert.assertNotNull("Resource not created!", hotel);
	}
}
