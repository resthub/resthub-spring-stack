package org.resthub.web.exception;

import org.fest.assertions.api.Assertions;
import org.resthub.web.Http;
import org.testng.annotations.Test;

public class HttpExceptionTest {
    
    @Test(expectedExceptions=IllegalArgumentException.class)
    public void testInvalidStatusCode() {
        ClientExceptionFactory.createHttpExceptionFromStatusCode(900);
    }
    
    @Test(expectedExceptions=IllegalArgumentException.class)
    public void test1xxStatusCode() {
        ClientExceptionFactory.createHttpExceptionFromStatusCode(100);
    }
    
    @Test(expectedExceptions=IllegalArgumentException.class)
    public void test2xxStatusCode() {
        ClientExceptionFactory.createHttpExceptionFromStatusCode(200);
    }
    
    @Test(expectedExceptions=IllegalArgumentException.class)
    public void test3xxStatusCode() {
        ClientExceptionFactory.createHttpExceptionFromStatusCode(300);
    }
    
    @Test
    public void test4xxUnsupportedStatusCode() {
        Assertions.assertThat(ClientExceptionFactory.createHttpExceptionFromStatusCode(444)).isInstanceOf(ClientException.class);
    }
    
    @Test
    public void test5xxUnsupportedStatusCode() {
        Assertions.assertThat(ClientExceptionFactory.createHttpExceptionFromStatusCode(555)).isInstanceOf(ClientException.class);
    }
    
    @Test
    public void testBadRequestStatusCode() {
        ClientException he = ClientExceptionFactory.createHttpExceptionFromStatusCode(Http.BAD_REQUEST);
        Assertions.assertThat(he).isNotNull().isInstanceOf(BadRequestClientException.class);
        Assertions.assertThat(he.getStatusCode()).isEqualTo(Http.BAD_REQUEST);
    }
    
    @Test
    public void testConflictStatusCode() {
        ClientException he = ClientExceptionFactory.createHttpExceptionFromStatusCode(Http.CONFLICT);
        Assertions.assertThat(he).isNotNull().isInstanceOf(ConflictClientException.class);
        Assertions.assertThat(he.getStatusCode()).isEqualTo(Http.CONFLICT);
    }
    
    @Test
    public void testInternalServerErrorStatusCode() {
        ClientException he = ClientExceptionFactory.createHttpExceptionFromStatusCode(Http.INTERNAL_SERVER_ERROR);
        Assertions.assertThat(he).isNotNull().isInstanceOf(InternalServerErrorClientException.class);
        Assertions.assertThat(he.getStatusCode()).isEqualTo(Http.INTERNAL_SERVER_ERROR);
    }
    
    @Test
    public void testNotFoundErrorStatusCode() {
        ClientException he = ClientExceptionFactory.createHttpExceptionFromStatusCode(Http.NOT_FOUND);
        Assertions.assertThat(he).isNotNull().isInstanceOf(NotFoundClientException.class);
        Assertions.assertThat(he.getStatusCode()).isEqualTo(Http.NOT_FOUND);
    }
    
    @Test
    public void testNotImplementedErrorStatusCode() {
        ClientException he = ClientExceptionFactory.createHttpExceptionFromStatusCode(Http.NOT_IMPLEMENTED);
        Assertions.assertThat(he).isNotNull().isInstanceOf(NotImplementedClientException.class);
        Assertions.assertThat(he.getStatusCode()).isEqualTo(Http.NOT_IMPLEMENTED);
    }
    
    @Test
    public void testUnauthorizedErrorStatusCode() {
        ClientException he = ClientExceptionFactory.createHttpExceptionFromStatusCode(Http.UNAUTHORIZED);
        Assertions.assertThat(he).isNotNull().isInstanceOf(UnauthorizedClientException.class);
        Assertions.assertThat(he.getStatusCode()).isEqualTo(Http.UNAUTHORIZED);
    }
    
    @Test
    public void testForbiddenErrorStatusCode() {
        ClientException he = ClientExceptionFactory.createHttpExceptionFromStatusCode(Http.FORBIDDEN);
        Assertions.assertThat(he).isNotNull().isInstanceOf(ForbiddenClientException.class);
        Assertions.assertThat(he.getStatusCode()).isEqualTo(Http.FORBIDDEN);
    }
    
    @Test
    public void testNotAcceptableErrorStatusCode() {
        ClientException he = ClientExceptionFactory.createHttpExceptionFromStatusCode(Http.NOT_ACCEPTABLE);
        Assertions.assertThat(he).isNotNull().isInstanceOf(NotAcceptableClientException.class);
        Assertions.assertThat(he.getStatusCode()).isEqualTo(Http.NOT_ACCEPTABLE);
    }
    
}
