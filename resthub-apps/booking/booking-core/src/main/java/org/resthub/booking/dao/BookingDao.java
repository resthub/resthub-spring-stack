package org.resthub.booking.dao;

import java.util.List;

import org.resthub.booking.model.Booking;
import org.resthub.booking.model.User;
import org.resthub.core.dao.GenericDao;

/**
 * @author bmeurant <Baptiste Meurant>
 */
public interface BookingDao extends GenericDao<Booking, Long> {

	/**
	 * @param user
	 *            user to find
	 * @return the list of booking for this user if it exists,
	 *         empty list otherwise
	 */
	List<Booking> findByUser(User user);

}