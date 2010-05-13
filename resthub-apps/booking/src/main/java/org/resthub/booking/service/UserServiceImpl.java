package org.resthub.booking.service;

import javax.inject.Inject;
import javax.inject.Named;
import org.resthub.booking.model.User;
import org.resthub.core.dao.GenericResourceDao;
import org.resthub.core.service.impl.GenericResourceServiceImpl;

@Named("userService")
public class UserServiceImpl extends GenericResourceServiceImpl<User, GenericResourceDao<User>> {

    @Inject
    @Named("userDao")
    @Override
    public void setDao(GenericResourceDao<User> userDao) {
        this.dao = userDao;
    }

}
