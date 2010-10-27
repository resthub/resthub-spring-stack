package org.resthub.booking.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.booking.dao.HotelDao;
import org.resthub.booking.model.Hotel;
import org.resthub.core.annotation.Auditable;
import org.resthub.core.service.GenericResourceServiceImpl;
import org.synyx.hades.domain.Page;
import org.synyx.hades.domain.Pageable;


@Named("hotelService")
public class HotelServiceImpl extends GenericResourceServiceImpl<Hotel, HotelDao> implements HotelService {

    @Inject
    @Named("hotelDao")
    @Override
    public void setDao(HotelDao hotelDao) {
        this.dao = hotelDao;
    }

	/**
	 * {@inheritDoc}
	 */
    @Auditable
	public Page<Hotel> find(final String query, final Pageable pageable) {
        if (query == null || query.isEmpty()) {
            return this.findAll(pageable);
        } 
        else {
            return this.dao.find(query, pageable);
        }
    }
    
    /**
	 * {@inheritDoc}
	 */
    @Auditable
	public List<Hotel> find(final String query) {
        if (query == null || query.isEmpty()) {
            return this.findAll();
        } 
        else {
        	Page<Hotel> resultList = this.dao.find(query, null);
        	if (resultList != null) {
        		return this.dao.find(query, null).asList();
        	}
        	else {
        		return new ArrayList<Hotel>();
        	}
        }
    }

	/**
	 * {@inheritDoc}
	 */
    @Auditable
    public void rebuildIndex() {
        this.dao.rebuildIndex();
    }
}
