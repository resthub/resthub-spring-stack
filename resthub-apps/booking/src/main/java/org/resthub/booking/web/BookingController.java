package org.resthub.booking.web;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Path;
import org.resthub.booking.model.Booking;
import org.resthub.core.service.GenericResourceService;
import org.resthub.web.controller.GenericResourceController;

@Path("/booking")
@Named("bookingController")
public class BookingController extends GenericResourceController<Booking, GenericResourceService<Booking>> {
    
    @Inject
    @Named("bookingService")
    @Override
    public void setService(GenericResourceService<Booking> service) {
        this.service = service;
    }
}
