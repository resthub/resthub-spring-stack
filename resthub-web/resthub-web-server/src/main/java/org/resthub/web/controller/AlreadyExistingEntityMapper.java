package org.resthub.web.controller;

import javax.inject.Named;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.resthub.core.exception.AlreadyExistingEntityException;

@Provider
@Named("alreadyExistingEntityMapper")
public class AlreadyExistingEntityMapper implements ExceptionMapper<AlreadyExistingEntityException> {

	@Override
	public Response toResponse(AlreadyExistingEntityException exception) {
		return Response.status(Response.Status.CONFLICT).build();

	}

}


