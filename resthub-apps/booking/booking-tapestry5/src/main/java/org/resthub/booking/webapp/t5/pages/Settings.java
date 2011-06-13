package org.resthub.booking.webapp.t5.pages;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.resthub.booking.model.User;
import org.resthub.booking.service.UserService;
import org.resthub.tapestry5.security.services.Authenticator;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Allows the user to modify password Inspirated from Tapestry5 booking sample
 * (http://tapestry.zones.apache.org:8180/tapestry5-hotel-booking)
 * 
 * @author bmeurant <Baptiste Meurant>
 * @author ccordenier
 */
public class Settings {
    @Inject
    @Service("userService")
    private UserService userService;

    @Inject
    private Messages messages;

    @Inject
    private Authenticator authenticator;

    @InjectPage
    private Signin signin;

    @Property
    private String password;

    @Property
    private String verifyPassword;

    @Component
    private Form settingsForm;

    public Object onSuccess() {
        if (!verifyPassword.equals(password)) {
            settingsForm.recordError(messages.get("error.verifypassword"));
            return null;
        }

        UserDetails userDetails = authenticator.getLoggedUser();
        authenticator.logout();

        User user = this.userService.findByUsername(userDetails.getUsername());
        user.setPassword(password);

        this.userService.update(user);

        return signin;
    }
}
