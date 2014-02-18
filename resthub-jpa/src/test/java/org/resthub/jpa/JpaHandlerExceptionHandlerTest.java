package org.resthub.jpa;

import org.fest.assertions.api.Assertions;
import org.hibernate.ObjectNotFoundException;
import org.resthub.common.model.RestError;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import static org.mockito.Mockito.mock;

public class JpaHandlerExceptionHandlerTest {

    private JpaHandlerExceptionHandler jpaHandler = new JpaHandlerExceptionHandler();
    private WebRequest webRequest = mock(WebRequest.class);

    @BeforeMethod
    public void setupTest() {

    }

    @Test
    public void testObjecNotFoundException() {
        ObjectNotFoundException ex = new ObjectNotFoundException(1L, "sample");
        ResponseEntity<Object> response = this.jpaHandler.handleCustomException(ex, this.webRequest);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isNotNull().isEqualTo(HttpStatus.NOT_FOUND);
        Assertions.assertThat(response.getBody()).isNotNull().isInstanceOf(RestError.class);
        RestError restError = (RestError)response.getBody();
        Assertions.assertThat(restError.getCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        Assertions.assertThat(restError.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.getReasonPhrase());
        Assertions.assertThat(restError.getThrowable()).isEqualTo(ex);
    }

    @Test
    public void testEntityNotFoundException() {
        EntityNotFoundException ex = new EntityNotFoundException();
        ResponseEntity<Object> response = this.jpaHandler.handleCustomException(ex, this.webRequest);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isNotNull().isEqualTo(HttpStatus.NOT_FOUND);
        Assertions.assertThat(response.getBody()).isNotNull().isInstanceOf(RestError.class);
        RestError restError = (RestError)response.getBody();
        Assertions.assertThat(restError.getCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        Assertions.assertThat(restError.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.getReasonPhrase());
        Assertions.assertThat(restError.getThrowable()).isEqualTo(ex);
    }

    @Test
    public void testEntityExistsException() {
        EntityExistsException ex = new EntityExistsException();
        ResponseEntity<Object> response = this.jpaHandler.handleCustomException(ex, this.webRequest);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isNotNull().isEqualTo(HttpStatus.CONFLICT);
        Assertions.assertThat(response.getBody()).isNotNull().isInstanceOf(RestError.class);
        RestError restError = (RestError)response.getBody();
        Assertions.assertThat(restError.getCode()).isEqualTo(HttpStatus.CONFLICT.value());
        Assertions.assertThat(restError.getStatus()).isEqualTo(HttpStatus.CONFLICT.getReasonPhrase());
        Assertions.assertThat(restError.getThrowable()).isEqualTo(ex);
    }

    @Test
     public void testDataIntegrityViolationException() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException("sample");
        ResponseEntity<Object> response = this.jpaHandler.handleCustomException(ex, this.webRequest);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isNotNull().isEqualTo(HttpStatus.CONFLICT);
        Assertions.assertThat(response.getBody()).isNotNull().isInstanceOf(RestError.class);
        RestError restError = (RestError)response.getBody();
        Assertions.assertThat(restError.getCode()).isEqualTo(HttpStatus.CONFLICT.value());
        Assertions.assertThat(restError.getStatus()).isEqualTo(HttpStatus.CONFLICT.getReasonPhrase());
        Assertions.assertThat(restError.getThrowable()).isEqualTo(ex);
    }

    @Test
    public void testOtherException() {
        Exception ex = new Exception();
        ResponseEntity<Object> response = this.jpaHandler.handleCustomException(ex, this.webRequest);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isNotNull().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        Assertions.assertThat(response.getBody()).isNull();
    }


}
