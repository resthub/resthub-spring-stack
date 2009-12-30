package org.resthub.identity.domain.model.dao;

import javax.inject.Named;

import org.resthub.core.domain.dao.jpa.JpaResourceDao;
import org.resthub.core.domain.model.User;

@Named("userDao")
public class JpaUserDao extends JpaResourceDao<User> {
    
}
