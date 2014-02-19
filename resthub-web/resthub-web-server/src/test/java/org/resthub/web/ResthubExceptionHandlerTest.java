package org.resthub.web;

import org.fest.assertions.api.Assertions;
import org.resthub.common.exception.NotFoundException;
import org.resthub.common.exception.NotImplementedException;
import org.resthub.common.model.RestError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.xml.bind.ValidationException;

import static org.mockito.Mockito.mock;

public class ResthubExceptionHandlerTest {

    private ResthubExceptionHandler resthubHandler = new ResthubExceptionHandler();
    private WebRequest webRequest = mock(WebRequest.class);

    @BeforeMethod
    public void setupTest() {

    }

    @Test
    public void testIllegalArgumentException() {
        IllegalArgumentException ex = new IllegalArgumentException();
        ResponseEntity<Object> response = this.resthubHandler.handleCustomException(ex, this.webRequest);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isNotNull().isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(response.getBody()).isNotNull().isInstanceOf(RestError.class);
        RestError restError = (RestError)response.getBody();
        Assertions.assertThat(restError.getCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        Assertions.assertThat(restError.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.getReasonPhrase());
        Assertions.assertThat(restError.getThrowable()).isEqualTo(ex);
    }

    @Test
    public void testValidationException() {
        ValidationException ex = new ValidationException("");
        ResponseEntity<Object> response = this.resthubHandler.handleCustomException(ex, this.webRequest);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isNotNull().isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(response.getBody()).isNotNull().isInstanceOf(RestError.class);
        RestError restError = (RestError)response.getBody();
        Assertions.assertThat(restError.getCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        Assertions.assertThat(restError.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.getReasonPhrase());
        Assertions.assertThat(restError.getThrowable()).isEqualTo(ex);
    }

    @Test
    public void testNotFoundException() {
        NotFoundException ex = new NotFoundException();
        ResponseEntity<Object> response = this.resthubHandler.handleCustomException(ex, this.webRequest);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isNotNull().isEqualTo(HttpStatus.NOT_FOUND);
        Assertions.assertThat(response.getBody()).isNotNull().isInstanceOf(RestError.class);
        RestError restError = (RestError)response.getBody();
        Assertions.assertThat(restError.getCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        Assertions.assertThat(restError.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.getReasonPhrase());
        Assertions.assertThat(restError.getThrowable()).isEqualTo(ex);
    }

    @Test
     public void testNotImplementedException() {
        NotImplementedException ex = new NotImplementedException();
        ResponseEntity<Object> response = this.resthubHandler.handleCustomException(ex, this.webRequest);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isNotNull().isEqualTo(HttpStatus.NOT_IMPLEMENTED);
        Assertions.assertThat(response.getBody()).isNotNull().isInstanceOf(RestError.class);
        RestError restError = (RestError)response.getBody();
        Assertions.assertThat(restError.getCode()).isEqualTo(HttpStatus.NOT_IMPLEMENTED.value());
        Assertions.assertThat(restError.getStatus()).isEqualTo(HttpStatus.NOT_IMPLEMENTED.getReasonPhrase());
        Assertions.assertThat(restError.getThrowable()).isEqualTo(ex);
    }

    @Test
    public void testOtherException() {
        Exception ex = new Exception();
        ResponseEntity<Object> response = this.resthubHandler.handleCustomException(ex, this.webRequest);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isNotNull().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        Assertions.assertThat(response.getBody()).isNull();
    }


}
