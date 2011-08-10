package org.resthub.booking.repository;

import java.util.List;

import org.resthub.booking.model.Booking;
import org.resthub.booking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author bmeurant <Baptiste Meurant>
 */
public interface BookingRepository extends JpaRepository<Booking, Long> {

	/**
	 * @param user
	 *            user to find
	 * @return the list of booking for this user if it exists, empty list otherwise
	 */
	List<Booking> findByUser(User user);
}
