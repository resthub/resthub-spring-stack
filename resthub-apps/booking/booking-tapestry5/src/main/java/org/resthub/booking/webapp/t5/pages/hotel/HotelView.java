package org.resthub.booking.webapp.t5.pages.hotel;

import org.apache.tapestry5.annotations.PageActivationContext;
import org.apache.tapestry5.annotations.Property;
import org.resthub.booking.model.Hotel;

/**
 * This page displays hotel details, and provide access to booking. Inspirated
 * by Tapestry5 booking sample
 * (http://tapestry.zones.apache.org:8180/tapestry5-hotel-booking)
 * 
 * @author bmeurant <Baptiste Meurant>
 * @author ccordenier
 */
public class HotelView {

    @SuppressWarnings("unused")
    @Property
    @PageActivationContext
    private Hotel hotel;

}
