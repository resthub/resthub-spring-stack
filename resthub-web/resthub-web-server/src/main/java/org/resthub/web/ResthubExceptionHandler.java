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
        HttpStatus status;

        if (ex instanceof IllegalArgumentException) {
            status = HttpStatus.BAD_REQUEST;
        } else if (ex instanceof ValidationException) {
            status = HttpStatus.BAD_REQUEST;
        } else if (ex instanceof NotFoundException) {
            status = HttpStatus.NOT_FOUND;
        } else if (ex instanceof NotImplementedException) {
            status = HttpStatus.NOT_IMPLEMENTED;
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
