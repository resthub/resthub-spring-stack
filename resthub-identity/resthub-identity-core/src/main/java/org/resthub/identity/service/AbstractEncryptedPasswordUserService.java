package org.resthub.identity.service;

import java.util.List;

import javax.inject.Named;

import org.jasypt.util.password.BasicPasswordEncryptor;
import org.resthub.core.service.GenericResourceServiceImpl;
import org.resthub.identity.dao.UserDao;
import org.resthub.identity.model.User;
import org.resthub.oauth2.provider.service.AuthenticationService;

/**
 * An Abstract Implementation of a UserService dealing with OAuth2
 * authentication <br/>
 * It is based on both GenericResourceServiceImpl, userService and
 * AuthenticationService <br/>
 * Password encryption is done with JASYPT
 * 
 * It is a bean whose name is userService
 * 
 * */
@Named("userService")
public abstract class AbstractEncryptedPasswordUserService extends
		GenericResourceServiceImpl<User, UserDao> implements UserService,
		AuthenticationService {

	/** A password encryptor, doing 1000 times MD5 has, with a 8 bytes salt */
	BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();

	/**
	 * Create a user with password encryption
	 * 
	 * @param user
	 *            the user to create
	 * */
	public User create(User user) {
		user.setPassword(passwordEncryptor.encryptPassword(user.getPassword()));
		System.out.println("create new user with pwd "+user.getPassword());
		dao.save(user);
		return user;
	}

	/**
	 * Update a user with password encryption
	 * 
	 * @param user
	 *            the suer to udpate
	 */
	public User update(User user) {
		user.setPassword(passwordEncryptor.encryptPassword(user.getPassword()));
		dao.save(user);
		return user;
	};

	/**
	 * {@inheritDoc}
	 */
	public User authenticateUser(String login, String password) {
		List<User> l = dao.findEquals("Login", login);
		User u = null;
		if (l != null) {
			for (User tmpU : l) {
				if (passwordEncryptor.checkPassword(password, tmpU
						.getPassword())) {
					u = tmpU;
					break;
				}
			}
		}
		return u;
	}
}
