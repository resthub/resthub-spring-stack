package org.resthub.booking.service;

import java.util.List;
import org.resthub.booking.model.Hotel;
import org.resthub.core.service.GenericResourceService;

/**
 * @author Guillaume Zurbach
 */
public interface HotelService extends GenericResourceService<Hotel> {
	public List<Hotel> search(String value);
}
