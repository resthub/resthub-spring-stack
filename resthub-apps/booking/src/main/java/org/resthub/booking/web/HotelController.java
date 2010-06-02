package org.resthub.booking.web;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Path;
import org.resthub.booking.model.Hotel;
import org.resthub.core.service.GenericResourceService;
import org.resthub.web.controller.GenericResourceController;

@Path("/hotel")
@Named("hotelController")
public class HotelController extends GenericResourceController<Hotel, GenericResourceService<Hotel>> {

	@Inject
	@Named("hotelService")
	@Override
	public void setService(GenericResourceService<Hotel> service) {
		this.service = service;
	}
}
