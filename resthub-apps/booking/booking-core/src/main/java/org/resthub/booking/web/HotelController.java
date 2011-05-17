package org.resthub.booking.web;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.resthub.booking.model.Hotel;
import org.resthub.booking.service.HotelService;
import org.resthub.web.controller.GenericController;
import org.resthub.web.response.PageResponse;
import org.synyx.hades.domain.PageRequest;

/**
 * @author Guillaume Zurbach
 */
@Path("/hotel")
@Named("hotelController")
public class HotelController extends GenericController<Hotel, Long, HotelService> {

	/**
	 * {@InheritDoc}
	 */
	@Inject
	@Named("hotelService")
	@Override
	public void setService(HotelService service) {
		this.service = service;
	}

	/**
	 * @return all hotels containing the value given in parameter
	 * If query string is empty, fetch all hotels in DB
	 */
	@GET
	@Path("/search")
	public PageResponse<Hotel> searchHotels(
			@QueryParam("q") String query,
			@QueryParam("page") @DefaultValue("0") Integer page,
			@QueryParam("size") @DefaultValue("5") Integer size) {
		
		PageResponse<Hotel> hotels;

		hotels = new PageResponse<Hotel>(
                    this.service.find(query, new PageRequest(page, size)));

		return hotels;
	}
}
