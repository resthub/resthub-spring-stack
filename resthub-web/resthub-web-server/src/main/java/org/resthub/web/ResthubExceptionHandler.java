package org.resthub.web;

import org.resthub.common.exception.NotFoundException;
import org.resthub.common.exception.NotImplementedException;
import org.resthub.common.model.RestError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import javax.xml.bind.ValidationException;

/**
 * RESThub default exception handler for most common exceptions.
 */
@ControllerAdvice
public class ResthubExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value={
            IllegalArgumentException.class,
            ValidationException.class,
            NotFoundException.class,
            NotImplementedException.class
    })
    public ResponseEntity<Object> handleCustomException(Exception ex, WebRequest request) {

        HttpHeaders headers = new HttpHeaders();

        if (ex instanceof IllegalArgumentException) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            return handleIllegalArgument((IllegalArgumentException) ex, headers, status, request);
        }
        else if (ex instanceof ValidationException) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            return handleValidation((ValidationException) ex, headers, status, request);
        }
        else if (ex instanceof NotFoundException) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            return handleNotFound((NotFoundException) ex, headers, status, request);
        }
        else if (ex instanceof NotImplementedException) {
            HttpStatus status = HttpStatus.NOT_IMPLEMENTED;
            return handleNotImplemented((NotImplementedException) ex, headers, status, request);
        } else {
            logger.warn("Unknown exception type: " + ex.getClass().getName());
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            return handleExceptionInternal(ex, null, headers, status, request);
        }
    }

    protected ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        RestError.Builder builder = new RestError.Builder();
        builder.setCode(status.value());
        builder.setStatus(status.getReasonPhrase());
        builder.setThrowable(ex);
        return handleExceptionInternal(ex, builder.build(), headers, status, request);
    }

    protected ResponseEntity<Object> handleValidation(ValidationException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        RestError.Builder builder = new RestError.Builder();
        builder.setCode(status.value()).setStatus(status.getReasonPhrase()).setThrowable(ex);
        return handleExceptionInternal(ex, builder.build(), headers, status, request);
    }

    protected ResponseEntity<Object> handleNotFound(NotFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        RestError.Builder builder = new RestError.Builder();
        builder.setCode(status.value()).setStatus(status.getReasonPhrase()).setThrowable(ex);
        return handleExceptionInternal(ex, builder.build(), headers, status, request);
    }

    protected ResponseEntity<Object> handleNotImplemented(NotImplementedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        RestError.Builder builder = new RestError.Builder();
        builder.setCode(status.value()).setStatus(status.getReasonPhrase()).setThrowable(ex);
        return handleExceptionInternal(ex, builder.build(), headers, status, request);
    }

}
