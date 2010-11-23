package org.resthub.identity.dao;

import javax.inject.Named;
import javax.persistence.NoResultException;

import org.resthub.core.dao.GenericJpaResourceDao;
import org.resthub.identity.model.User;

/**
 * The JpaUserDao class is based on both {@link GenericJpaResourceDao} and
 * {@link UserDao}<br/>
 * 
 * It is a bean whose name is userDao
 * */
@Named("userDao")
public class JpaUserDao extends GenericJpaResourceDao<User> implements UserDao {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User findByIdWithGroups(Long id) {
		User u = null;
		try {
			u = this.getEntityManager().createQuery("select u from User u " +
					"left join fetch u.groups where u.id = :id", User.class)
					.setParameter("id", id)
					.getSingleResult();
		} catch (NoResultException exc) {
			// nothing to do.
		}
		return u;
	} // findByIdWithGroups().
}