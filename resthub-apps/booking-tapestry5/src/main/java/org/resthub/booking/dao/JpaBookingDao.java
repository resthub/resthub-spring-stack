package org.resthub.booking.dao;

import javax.inject.Named;

import org.resthub.booking.model.Booking;
import org.resthub.core.dao.GenericJpaResourceDao;


@Named("bookingDao")
public class JpaBookingDao extends GenericJpaResourceDao<Booking> {

}
