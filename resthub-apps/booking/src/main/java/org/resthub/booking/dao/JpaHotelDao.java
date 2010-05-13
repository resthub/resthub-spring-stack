package org.resthub.booking.dao;

import javax.inject.Named;
import org.resthub.booking.model.Hotel;
import org.resthub.core.dao.jpa.GenericJpaResourceDao;

@Named("hotelDao")
public class JpaHotelDao extends GenericJpaResourceDao<Hotel> {

}
