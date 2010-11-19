package org.resthub.identity.dao;

import java.util.List;

import javax.inject.Named;
import javax.persistence.Query;

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

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * {@inheritDoc} 
	 * */
	public User getUserByAuthenticationInformation(String login, String password) {

		Query query = this.getEntityManager().createQuery(
				"Select u from User u where u.login= ?1 and u.password =?2");
		query.setParameter(1, login);
		query.setParameter(2, password);

		User u = null;
		List<User> lU = query.getResultList();
		int size = (lU == null) ? 0 : lU.size();
		if (size != 0) {
			u = lU.get(0);
		}
		return u;
	}
}