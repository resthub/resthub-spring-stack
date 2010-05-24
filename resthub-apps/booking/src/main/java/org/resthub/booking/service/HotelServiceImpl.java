package org.resthub.booking.service;

import javax.inject.Inject;
import javax.inject.Named;
import org.resthub.booking.model.Hotel;
import org.resthub.core.dao.GenericResourceDao;
import org.resthub.core.service.GenericResourceServiceImpl;

@Named("hotelService")
public class HotelServiceImpl extends GenericResourceServiceImpl<Hotel, GenericResourceDao<Hotel>> {

    @Inject
    @Named("hotelDao")
    @Override
    public void setDao(GenericResourceDao<Hotel> hotelDao) {
        this.dao = hotelDao;
    }

}
