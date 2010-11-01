package org.resthub.booking.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.booking.dao.BookingDao;
import org.resthub.booking.dao.UserDao;
import org.resthub.booking.model.Booking;
import org.resthub.core.service.GenericResourceServiceImpl;
import org.springframework.util.Assert;

/**
 * @author Guillaume Zurbach
 * @author bmeurant <Baptiste Meurant>
 */
@Named("bookingService")
public class BookingServiceImpl extends
		GenericResourceServiceImpl<Booking, BookingDao> implements
		BookingService {

	/**
	 * {@InheritDoc}
	 */
	@Inject
	@Named("bookingDao")
	@Override
	public void setDao(BookingDao bookingDao) {
		this.dao = bookingDao;
	}
	
	@Inject
	@Named("userDao")
	private UserDao userDao;
	
	/**
	 * {@InheritDoc}
	 */
	public List<Booking> findByUserId(Long userId) {
		Assert.notNull(userId, "User ID can't be null");

		return dao.findByUser(this.userDao.readByPrimaryKey(userId));
	}

}
