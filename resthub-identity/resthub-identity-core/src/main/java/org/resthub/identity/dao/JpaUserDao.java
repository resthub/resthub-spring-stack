package org.resthub.identity.dao;

import java.util.List;

import javax.inject.Named;
import javax.persistence.Query;

import org.resthub.core.dao.GenericJpaDao;
import org.resthub.identity.model.User;

/**
 * The JpaUserDao class is based on both {@link GenericJpaResourceDao} and
 * {@link UserDao}<br/>
 * 
 * It is a bean whose name is userDao
 * */
@Named("userDao")
public class JpaUserDao extends GenericJpaDao<User, Long> implements UserDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getUsersFromGroup(String groupName) {
        String queryString = "select u from " + this.getDomainClass().getName()
                + " u JOIN u.groups g where g.name=:groupName";
        Query q = this.getEntityManager().createQuery(queryString);
        q.setParameter("groupName", groupName);
        return q.getResultList();
    }
}
