package org.resthub.identity.dao;

import java.util.List;

import javax.inject.Named;

import org.resthub.core.dao.GenericJpaResourceDao;
import org.resthub.identity.model.User;

@Named("userDao")
public class JpaUserDao extends GenericJpaResourceDao<User> implements UserDao {

	@Override
	public User getUserByAuthenticationInformation(String login, String password) {
		List<User> users = this.findEquals("login", login);
		User returnUser=null;
		for(User u :users){
			if(u.getPassword().equals(password)){
				returnUser=u;
			break;}
		}
		return returnUser;
		
	}
}