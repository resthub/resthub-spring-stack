package org.resthub.booking.web;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.booking.service.HotelService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Provide access to indexation functions from HibernateSearch
 * 
 * @author Nicolas Carlier
 */
@Controller @RequestMapping("/api/lucene")
public class LuceneIndexControler {

    private HotelService hotelService;

    @Inject
    @Named("hotelService")
    public void setService(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    /**
     * rebuild the HibernateSearch index
     * 
     * @return ok response or error code
     */

    @RequestMapping(method = RequestMethod.POST, value = "rebuild") @ResponseStatus(HttpStatus.OK) @ResponseBody
    public void rebuildIndex() {
        this.hotelService.rebuildIndex();
    }
}
