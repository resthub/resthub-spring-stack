package org.resthub.booking.service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.lucene.queryParser.ParseException;
import org.resthub.booking.dao.HotelDao;
import org.resthub.booking.model.Hotel;
import org.resthub.core.annotation.Auditable;
import org.resthub.core.dao.GenericResourceDao;
import org.resthub.core.service.GenericResourceServiceImpl;

@Named("hotelService")
public class HotelServiceImpl extends GenericResourceServiceImpl<Hotel, HotelDao> implements HotelService {

    @Inject
    @Named("hotelDao")
    @Override
    public void setDao(HotelDao hotelDao) {
        this.dao = hotelDao;
    }

    @Override
    @Auditable
    public List<Hotel> find(String query) {
		try {
			return this.dao.find(query);
		} catch (ParseException ex) {
			Logger.getLogger(HotelServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
    }

    @Override
    @Auditable
    public void rebuildIndex() {
        this.dao.rebuildIndex();
    }
}
