package   org.resthub.oauth2.provider.service;

import java.util.List;

import javax.inject.Named;

import org.resthub.identity.model.User;
import org.resthub.identity.service.UserServiceImpl;

/*
 * This implementation is based on the UserService implementation from resthub-identity<br/>
 * The user Identifier is the Login
 * 
 * */
@Named("authenticationService")
public class AuthenticationServiceImpl extends UserServiceImpl implements
		AuthenticationService {

	@Override
	public String getUser(String userName, String password) {
		User u = this.authenticateUser(userName, password);
		return (u!=null)? u.getLogin() : null;
		}

	@Override
	public List<String> getUserPermissions(String userId) {
		return this.findByLogin(userId).getPermissions();

	}

}
