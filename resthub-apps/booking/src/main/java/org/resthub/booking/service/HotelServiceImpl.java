package org.resthub.booking.service;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import org.resthub.booking.model.Hotel;
import org.resthub.core.dao.GenericResourceDao;
import org.resthub.core.service.GenericResourceServiceImpl;

@Named("hotelService")
public class HotelServiceImpl extends GenericResourceServiceImpl<Hotel, GenericResourceDao<Hotel>> implements HotelService {

    @Inject
    @Named("hotelDao")
    @Override
    public void setDao(GenericResourceDao<Hotel> hotelDao) {
        this.dao = hotelDao;
    }

	@Override
	/**
	 * Fetch all hotels containing the value given in parameter
	 */
	public List<Hotel> search(String value) {
		StringBuilder sb = new StringBuilder();
		sb.append("%");
		sb.append(value);
		sb.append("%");
		return dao.findLike("name", sb.toString());
	}

}
