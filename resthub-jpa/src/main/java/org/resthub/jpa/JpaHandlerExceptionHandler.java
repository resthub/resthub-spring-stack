package org.resthub.jpa;

import org.hibernate.ObjectNotFoundException;
import org.resthub.common.model.RestError;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

/**
 * Spring MVC exception handler user to map JPA related exception to HTTP error codes.
 * Spring MVC is an optional dependency, it will be used only if already imported by your application.
 */
@ControllerAdvice
public class JpaHandlerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value={
            ObjectNotFoundException.class,
            EntityNotFoundException.class,
            EntityExistsException.class,
            DataIntegrityViolationException.class
    })
    public ResponseEntity<Object> handleCustomException(Exception ex, WebRequest request) {

        HttpHeaders headers = new HttpHeaders();
        HttpStatus status;

        if (ex instanceof ObjectNotFoundException) {
            status = HttpStatus.NOT_FOUND;
        } else if (ex instanceof EntityNotFoundException) {
            status = HttpStatus.NOT_FOUND;
        } else if (ex instanceof EntityExistsException) {
            status = HttpStatus.CONFLICT;
        } else if (ex instanceof DataIntegrityViolationException) {
            status = HttpStatus.CONFLICT;
        } else {
            logger.warn("Unknown exception type: " + ex.getClass().getName());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            return handleExceptionInternal(ex, null, headers, status, request);
        }

        return handleExceptionInternal(ex, buildRestError(ex, status), headers, status, request);
    }

    private RestError buildRestError(Exception ex, HttpStatus status) {
        RestError.Builder builder = new RestError.Builder();
        builder.setCode(status.value()).setStatus(status.getReasonPhrase()).setThrowable(ex);
        return builder.build();
    }

}