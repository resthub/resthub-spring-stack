package org.resthub.identity.domain.model.dao;

import org.resthub.core.domain.dao.jpa.JpaResourceDao;
import org.resthub.core.domain.model.User;
import org.springframework.stereotype.Repository;

@Repository("userDao")
public class JpaUserDao extends JpaResourceDao<User> {
    
}
