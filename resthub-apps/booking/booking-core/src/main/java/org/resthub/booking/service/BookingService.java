package org.resthub.booking.service;

import java.util.List;

import org.resthub.booking.model.Booking;
import org.resthub.core.service.GenericService;

/**
 * @author Guillaume Zurbach
 * @author bmeurant <Baptiste Meurant>
 */
public interface BookingService extends GenericService<Booking, Long> {

	/**
	 * @param userId
	 * @return all bookings made by user identified by userId
	 */
	List<Booking> findByUserId(Long userId);
}
