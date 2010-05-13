/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.resthub.booking.service;

import javax.inject.Inject;
import javax.inject.Named;
import org.resthub.booking.model.Booking;
import org.resthub.core.dao.GenericResourceDao;
import org.resthub.core.service.impl.GenericResourceServiceImpl;

@Named("bookingService")
public class BookingServiceImpl extends GenericResourceServiceImpl<Booking, GenericResourceDao<Booking>> {

    @Inject
    @Named("bookingDao")
    @Override
    public void setDao(GenericResourceDao<Booking> bookingDao) {
        this.dao = bookingDao;
    }

}
