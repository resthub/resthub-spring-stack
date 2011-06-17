package org.resthub.booking.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.booking.dao.UserDao;
import org.resthub.booking.model.User;
import org.resthub.core.service.GenericServiceImpl;

/**
 * @author Guillaume Zurbach
 * @author bmeurant <Baptiste Meurant>
 */
@Named("userService")
public class UserServiceImpl extends GenericServiceImpl<User, Long, UserDao> implements UserService {

    /**
     * {@InheritDoc}
     */
    @Inject
    @Named("userDao")
    @Override
    public void setDao(UserDao userDao) {
        this.dao = userDao;
    }

    /**
     * {@InheritDoc}
     */
    public User findByUsername(String username) {

        List<User> users = this.dao.findEquals("username", username);

        if (users.size() > 1) {
            throw new IllegalArgumentException("username should be unique");
        }

        if (users.isEmpty()) {
            return null;
        }

        return users.get(0);
    }

    /**
     * {@InheritDoc}
     */
    public User findByEmail(String email) {

        List<User> users = this.dao.findEquals("email", email);

        if (users.size() > 1) {
            throw new IllegalArgumentException("email should be unique");
        }

        if (users.isEmpty()) {
            return null;
        }

        return users.get(0);
    }

}
