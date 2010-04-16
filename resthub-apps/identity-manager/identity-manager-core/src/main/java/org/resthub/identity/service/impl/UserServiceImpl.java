package org.resthub.identity.service.impl;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.service.impl.ResourceGenericServiceImpl;
import org.resthub.identity.domain.dao.UserDao;
import org.resthub.identity.domain.model.User;

@Named("userService")
public class UserServiceImpl extends ResourceGenericServiceImpl<User, UserDao> {

    @Override
    @Inject
    @Named("userDao")
    public void setResourceDao(UserDao resourceDao) {
        super.setResourceDao(resourceDao);
    }

}
