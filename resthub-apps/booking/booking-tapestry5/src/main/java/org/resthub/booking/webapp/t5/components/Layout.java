package org.resthub.booking.webapp.t5.components;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.resthub.tapestry5.security.services.Authenticator;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Layout component for pages of resthub tapestry5-booking sample application.
 * Inspirated by Tapestry5 booking sample
 * (http://tapestry.zones.apache.org:8180/tapestry5-hotel-booking)
 * 
 * @author bmeurant <Baptiste Meurant>
 * @author ccordenier
 */
@Import(stylesheet = { "context:static/css/booking.css", "context:static/css/t5-booking.css" })
public class Layout {
    @Inject
    private Authenticator authenticator;

    public UserDetails getUser() {
        return this.authenticator.getLoggedUser();
    }

    public Boolean isLoggedIn() {
        return authenticator.isLoggedIn();
    }

    public void onActionFromLogout() {
        this.authenticator.logout();
    }
}
