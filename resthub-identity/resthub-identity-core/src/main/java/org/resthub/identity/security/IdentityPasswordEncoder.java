package org.resthub.identity.security;

import javax.inject.Inject;
import javax.inject.Named;

import org.jasypt.util.password.PasswordEncryptor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.encoding.PasswordEncoder;

@Named("identityPasswordEncoder")
public class IdentityPasswordEncoder implements PasswordEncoder {
	
	@Inject
	@Named("passwordEncryptor")
	PasswordEncryptor passwordEncryptor;

	@Override
	public String encodePassword(String rawPass, Object salt) throws DataAccessException {
		return passwordEncryptor.encryptPassword(rawPass);
	}

	@Override
	public boolean isPasswordValid(String encPass, String rawPass, Object salt) throws DataAccessException {
		return passwordEncryptor.checkPassword(rawPass, encPass);
	}

}
