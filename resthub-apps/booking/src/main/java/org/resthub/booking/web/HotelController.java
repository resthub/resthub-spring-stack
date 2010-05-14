package org.resthub.booking.web;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Path;
import org.resthub.booking.model.Hotel;
import org.resthub.core.service.GenericService;
import org.resthub.web.controller.GenericResourceController;

@Path("/hotel")
@Named("hotelController")
public class HotelController extends GenericResourceController<Hotel> {
    
    @Inject
    @Named("hotelService")
    @Override
    public void setService(GenericService<Hotel, Long> service) {
        this.service = service;
    }
}
