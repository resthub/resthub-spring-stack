package org.resthub.web.oauth2;

import java.io.IOException;

import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 *  Spring OAuth2 authentication entry point for bearer token
 */
@Named("oauth2ProcessingFilterEntryPoint")
public class OAuth2ProcessingFilterEntryPoint implements AuthenticationEntryPoint {

    private String realmName;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        StringBuilder headerValue = new StringBuilder("Bearer");
        if (realmName != null) {
            headerValue.append(" realm=\"").append(realmName).append('"');
        }
        response.addHeader("WWW-Authenticate", headerValue.toString());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }

    public String getRealmName() {
        return realmName;
    }

    public void setRealmName(String realmName) {
        this.realmName = realmName;
    }

}
