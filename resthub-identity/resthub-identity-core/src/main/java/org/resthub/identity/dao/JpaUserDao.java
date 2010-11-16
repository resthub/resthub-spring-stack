package org.resthub.identity.dao;

import javax.inject.Named;
import javax.persistence.Query;

import org.resthub.core.dao.GenericJpaResourceDao;
import org.resthub.identity.model.User;

@Named("userDao")
public class JpaUserDao extends GenericJpaResourceDao<User> implements UserDao {

	@Override
	public User getUserByAuthenticationInformation(String login, String password) {

		Query query = this
				.getEntityManager()
				.createQuery(
						"Select u from User u where u.login= ?1 and u.password =?2");
		query.setParameter(1, login);
		query.setParameter(2, password);

		User u=null;
		try{ u= (User)query.getSingleResult();}catch(Exception e){e.printStackTrace();/*Nothing to do*/}
		return u;
	}
}