package org.resthub.identity.service.impl;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.domain.model.User;
import org.resthub.core.service.impl.ResourceServiceImpl;
import org.resthub.identity.domain.model.dao.JpaUserDao;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl extends ResourceServiceImpl<User, JpaUserDao> {

    @Override
    @Inject
    @Named("userDao")
    public void setResourceDao(JpaUserDao resourceDao) {
        super.setResourceDao(resourceDao);
    }

}
