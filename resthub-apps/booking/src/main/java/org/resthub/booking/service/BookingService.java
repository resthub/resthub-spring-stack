/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.resthub.booking.service;

import java.util.List;
import org.resthub.booking.model.Booking;
import org.resthub.core.service.GenericResourceService;

/**
 * @author Guillaume Zurbach
 */
public interface BookingService extends GenericResourceService<Booking> {
	public List<Booking> findByUserId(String userId);
}
