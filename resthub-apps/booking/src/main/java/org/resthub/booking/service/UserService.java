/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.resthub.booking.service;

import org.resthub.booking.model.User;
import org.resthub.core.service.GenericResourceService;

/**
 *
 * @author bouiaw
 */
public interface UserService extends GenericResourceService<User> {
    Boolean checkCredentials(String username, String password);
}
