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

        if (ex instanceof ObjectNotFoundException) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            return handleObjectNotFound((ObjectNotFoundException) ex, headers, status, request);
        }
        else if (ex instanceof EntityNotFoundException) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            return handleEntityNotFound((EntityNotFoundException) ex, headers, status, request);
        }
        else if (ex instanceof EntityExistsException) {
            HttpStatus status = HttpStatus.CONFLICT;
            return handleEntityExists((EntityExistsException) ex, headers, status, request);
        } else if (ex instanceof DataIntegrityViolationException) {
            HttpStatus status = HttpStatus.CONFLICT;
            return handleDataIntegrityViolation((DataIntegrityViolationException) ex, headers, status, request);
        }
        else {
            logger.warn("Unknown exception type: " + ex.getClass().getName());
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            return handleExceptionInternal(ex, null, headers, status, request);
        }
    }

    protected ResponseEntity<Object> handleObjectNotFound(ObjectNotFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        RestError.Builder builder = new RestError.Builder();
        builder.setCode(status.value()).setStatus(status.getReasonPhrase()).setThrowable(ex);
        return handleExceptionInternal(ex, builder.build(), headers, status, request);
    }

    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        RestError.Builder builder = new RestError.Builder();
        builder.setCode(status.value()).setStatus(status.getReasonPhrase()).setThrowable(ex);
        return handleExceptionInternal(ex, builder.build(), headers, status, request);
    }

    protected ResponseEntity<Object> handleEntityExists(EntityExistsException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        RestError.Builder builder = new RestError.Builder();
        builder.setCode(status.value()).setStatus(status.getReasonPhrase()).setThrowable(ex);
        return handleExceptionInternal(ex, builder.build(), headers, status, request);
    }

    protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        RestError.Builder builder = new RestError.Builder();
        builder.setCode(status.value()).setStatus(status.getReasonPhrase()).setThrowable(ex);
        return handleExceptionInternal(ex, builder.build(), headers, status, request);
    }

}