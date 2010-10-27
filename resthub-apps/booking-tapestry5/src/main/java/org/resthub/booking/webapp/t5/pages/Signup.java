package org.resthub.booking.webapp.t5.pages;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.resthub.booking.model.User;
import org.resthub.booking.service.UserService;
import org.resthub.tapestry5.security.AuthenticationException;
import org.resthub.tapestry5.security.services.Authenticator;


/**
 * This page the user can create an account
 * 
 * @author karesti
 * @version 1.0
 */
public class Signup {

	@Property
	private String verifyPassword;

	@SuppressWarnings("unused")
	@Property
	private String kaptcha;

	@Inject
	@Service("userService")
	private UserService userService;
	
	@Inject
	private Authenticator authenticator;

	@Component
	private Form registerForm;

	@Inject
	private Messages messages;

	@Property
	private User user;
	
	public void onActivate() {
		if (this.user == null) {
			this.user = new User();
		}
	}

	@OnEvent(value = EventConstants.SUCCESS, component = "RegisterForm")
	public Object proceedSignup() {
		if (!verifyPassword.equals(user.getPassword())) {
			registerForm.recordError(messages.get("error.verifypassword"));

			return null;
		}

		User userVerif = userService.findByUsername(user.getUsername());

		if (userVerif != null) {
			registerForm.recordError(messages.get("error.usernameExists"));

			return null;
		}

		if (!checkEmailUnicity() || !checkUsernameUnicity()) {
			return null;
		}

		userService.create(user);

		try {
			this.authenticator.login(user.getUsername(), user.getPassword());
			return Search.class;
		}
		catch (AuthenticationException e) {
			registerForm.recordError("Authentication process has failed");
			return this;
		}
	}

	private Boolean checkEmailUnicity() {
		User userVerif;
		userVerif = userService.findByEmail(user.getEmail());

		if (userVerif != null) {
			registerForm.recordError(messages.get("error.emailExists"));
			return false;
		}

		return true;
	}

	private Boolean checkUsernameUnicity() {
		User userVerif;
		userVerif = userService.findByUsername(user.getUsername());

		if (userVerif != null) {
			registerForm.recordError(messages.get("error.usernameExists"));
			return false;
		}

		return true;
	}
}
