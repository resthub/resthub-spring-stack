package org.resthub.tapestry5.security.services;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;

public interface LoginService {

	public abstract Boolean executeLogin(String i_username, String i_password,
			List<GrantedAuthority> i_authorities);

}