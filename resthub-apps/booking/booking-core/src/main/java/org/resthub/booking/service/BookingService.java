package org.resthub.booking.service;

import java.util.List;

import org.resthub.booking.model.Booking;
import org.resthub.core.service.GenericResourceService;

/**
 * @author Guillaume Zurbach
 * @author Baptiste Meurant
 */
public interface BookingService extends GenericResourceService<Booking> {

	/**
	 * @param userId
	 * @return all bookings made by user identified by userId
	 */
	List<Booking> findByUserId(Long userId);
}
