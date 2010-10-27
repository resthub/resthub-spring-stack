/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.resthub.booking.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.booking.model.Booking;
import org.resthub.core.dao.GenericResourceDao;
import org.resthub.core.service.GenericResourceServiceImpl;
import org.springframework.util.Assert;


@Named("bookingService")
public class BookingServiceImpl extends GenericResourceServiceImpl<Booking, GenericResourceDao<Booking>> implements BookingService {

	@Inject
	@Named("bookingDao")
	@Override
	public void setDao(GenericResourceDao<Booking> bookingDao) {
		this.dao = bookingDao;
	}

	/**
	 * Fetch all bookings made by user identified by userId.
	**/
	public List<Booking> findByUserId(String userId) {
		Assert.notNull(userId, "User ID can't be null");
		return dao.findEquals("user_id", userId);
	}

}
