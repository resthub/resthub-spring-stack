package org.resthub.booking.service;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import org.resthub.booking.model.User;
import org.resthub.core.dao.GenericResourceDao;
import org.resthub.core.service.impl.GenericResourceServiceImpl;

@Named("userService")
public class UserServiceImpl extends GenericResourceServiceImpl<User, GenericResourceDao<User>> implements UserService {

    @Inject
    @Named("userDao")
    @Override
    public void setDao(GenericResourceDao<User> userDao) {
        this.dao = userDao;
    }

    @Override
    /**
     * Naive implementation of checkLogin
     * Real life implementation should store and compare encrypted passwords
    **/
    public Boolean checkCredentials(String username, String password) {
        List<User> users = this.dao.findEquals("username", username);

        if((users!=null) && (users.size() == 1) && users.get(0).getPassword().equals(password)) {
            return true;
        }
        return false;
    }

}
