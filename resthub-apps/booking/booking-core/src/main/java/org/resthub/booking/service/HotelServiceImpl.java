package org.resthub.booking.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.booking.dao.HotelDao;
import org.resthub.booking.model.Hotel;
import org.resthub.core.service.GenericResourceServiceImpl;
import org.synyx.hades.domain.Page;
import org.synyx.hades.domain.Pageable;

/**
 * @author Guillaume Zurbach
 * @author bmeurant <Baptiste Meurant>
 */
@Named("hotelService")
public class HotelServiceImpl extends
        GenericResourceServiceImpl<Hotel, HotelDao> implements HotelService {

    /**
     * {@InheritDoc}
     */
    @Inject
    @Named("hotelDao")
    @Override
    public void setDao(HotelDao hotelDao) {
        this.dao = hotelDao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Hotel> find(final String query, final Pageable pageable) {
        if (query == null || query.isEmpty()) {
            return this.findAll(pageable);
        } else {
            return this.dao.find(query, pageable);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Hotel> find(final String query) {
        if (query == null || query.isEmpty()) {
            return this.findAll();
        } else {
            Page<Hotel> resultList = this.dao.find(query, null);
            if (resultList == null) {
                return new ArrayList<Hotel>();
            } else {
                return this.dao.find(query, null).asList();

            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rebuildIndex() {
        this.dao.rebuildIndex();
    }
}
