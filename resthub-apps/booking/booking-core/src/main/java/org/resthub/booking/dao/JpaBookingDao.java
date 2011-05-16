package org.resthub.booking.dao;

import java.util.List;

import javax.inject.Named;
import javax.persistence.Query;

import org.resthub.booking.model.Booking;
import org.resthub.booking.model.User;
import org.resthub.core.dao.GenericJpaDao;

/**
 * @author Guillaume Zurbach
 */
@Named("bookingDao")
public class JpaBookingDao extends GenericJpaDao<Booking, Long> implements
		BookingDao {

	/**
	 * {@InheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Booking> findByUser(User user) {
		
		Query query = this.getEntityManager().createQuery("select b from Booking b where user = ?1");
		query.setParameter(1, user);
		return (List<Booking>) query.getResultList();

	}

}
