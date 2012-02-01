package org.resthub.booking.web;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.booking.model.Hotel;
import org.resthub.booking.service.HotelService;
import org.resthub.web.controller.GenericControllerImpl;
import org.resthub.web.response.PageResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Guillaume Zurbach
 */
@Controller @RequestMapping("/api/hotel")
public class HotelController extends GenericControllerImpl<Hotel, Long, HotelService> {

    /**
     * {@inheritDoc}
     */
    @Inject
    @Named("hotelService")
    @Override
    public void setService(HotelService service) {
        this.service = service;
    }

    /**
     * @return all hotels containing the value given in parameter If query
     *         string is empty, fetch all hotels in DB
     */
    @RequestMapping(method = RequestMethod.GET, value = "search") @ResponseBody
    public PageResponse<Hotel> searchHotels(@RequestParam("q") String query,
            								@RequestParam("page") Integer page, 
            								@RequestParam("size") Integer size) {
    	
    	page = (page == null) ? 0 : page;
        size = (size == null) ? 5 : size;

        PageResponse<Hotel> hotels = new PageResponse<Hotel>(this.service.find(query, new PageRequest(page, size)));

        return hotels;
    }
}
