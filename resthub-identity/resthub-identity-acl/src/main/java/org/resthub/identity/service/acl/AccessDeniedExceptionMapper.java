package org.resthub.identity.service.acl;

import javax.inject.Named;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.springframework.security.access.AccessDeniedException;

/**
 * This provider is intended to translate Spring Security AccessDeniedExceptions
 * to HTTP 403 FORBIDDEN errors.
 */
@Provider
@Named("accessDeniedExceptionMapper")
public class AccessDeniedExceptionMapper implements ExceptionMapper<AccessDeniedException> {

    /**
     * {@inheritDoc}<br/>
     * <br/>
     * Returns an forbidden responses.
     */
    @Override
    public Response toResponse(AccessDeniedException exception) {
        return Response.status(Status.FORBIDDEN).build();
    } // toResponse().

} // class AccessDeniedExceptionMapper.

