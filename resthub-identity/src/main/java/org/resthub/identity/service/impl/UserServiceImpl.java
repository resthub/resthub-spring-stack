package org.resthub.identity.service.impl;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.service.impl.AbstractResourceServiceImpl;
import org.resthub.identity.domain.model.User;
import org.resthub.identity.domain.model.dao.UserDao;

@Named("userService")
public class UserServiceImpl extends AbstractResourceServiceImpl<User, UserDao> {

    @Override
    @Inject
    @Named("userDao")
    public void setResourceDao(UserDao resourceDao) {
        super.setResourceDao(resourceDao);
    }

}
