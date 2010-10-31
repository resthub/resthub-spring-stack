package org.resthub.booking.webapp.t5.components.hotel;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.resthub.booking.model.Hotel;


/**
 * @author Baptiste Meurant
 */
public class HotelDisplay {

	@SuppressWarnings("unused")
	@Parameter(required = true)
	@Property
	private Hotel hotel;
	
}
