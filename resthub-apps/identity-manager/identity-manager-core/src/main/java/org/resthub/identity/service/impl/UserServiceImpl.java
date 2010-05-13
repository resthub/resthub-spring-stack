package org.resthub.identity.service.impl;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.service.impl.GenericResourceServiceImpl;
import org.resthub.identity.dao.UserDao;
import org.resthub.identity.model.User;

@Named("userService")
public class UserServiceImpl extends GenericResourceServiceImpl<User, UserDao> {

    @Inject
    @Named("userDao")
    public void setResourceDao(UserDao userDao) {
        super.setDao(userDao);
    }

}
