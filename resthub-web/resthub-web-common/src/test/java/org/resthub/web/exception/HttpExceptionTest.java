package org.resthub.web.exception;

import org.fest.assertions.api.Assertions;
import org.resthub.web.Http;
import org.testng.annotations.Test;

public class HttpExceptionTest {
    
    @Test(expectedExceptions=IllegalArgumentException.class)
    public void testUnvalidStatusCode() {
        HttpExceptionFactory.createHttpExceptionFromStatusCode(900);
    }
    
    @Test(expectedExceptions=IllegalArgumentException.class)
    public void test1xxStatusCode() {
        HttpExceptionFactory.createHttpExceptionFromStatusCode(100);
    }
    
    @Test(expectedExceptions=IllegalArgumentException.class)
    public void test2xxStatusCode() {
        HttpExceptionFactory.createHttpExceptionFromStatusCode(200);
    }
    
    @Test(expectedExceptions=IllegalArgumentException.class)
    public void test3xxStatusCode() {
        HttpExceptionFactory.createHttpExceptionFromStatusCode(300);
    }
    
    @Test
    public void test4xxUnsupportedStatusCode() {
        Assertions.assertThat(HttpExceptionFactory.createHttpExceptionFromStatusCode(444)).isInstanceOf(HttpClientErrorException.class);
    }
    
    @Test
    public void test5xxUnsupportedStatusCode() {
        Assertions.assertThat(HttpExceptionFactory.createHttpExceptionFromStatusCode(555)).isInstanceOf(HttpServerErrorException.class);
    }
    
    @Test
    public void testBadRequestStatusCode() {
        HttpException he = HttpExceptionFactory.createHttpExceptionFromStatusCode(Http.BAD_REQUEST);
        Assertions.assertThat(he).isNotNull().isInstanceOf(BadRequestException.class);
        Assertions.assertThat(he.getStatusCode()).isEqualTo(Http.BAD_REQUEST);
    }
    
    @Test
    public void testConflictStatusCode() {
        HttpException he = HttpExceptionFactory.createHttpExceptionFromStatusCode(Http.CONFLICT);
        Assertions.assertThat(he).isNotNull().isInstanceOf(ConflictException.class);
        Assertions.assertThat(he.getStatusCode()).isEqualTo(Http.CONFLICT);
    }
    
    @Test
    public void testInternalServerErrorStatusCode() {
        HttpException he = HttpExceptionFactory.createHttpExceptionFromStatusCode(Http.INTERNAL_SERVER_ERROR);
        Assertions.assertThat(he).isNotNull().isInstanceOf(InternalServerErrorException.class);
        Assertions.assertThat(he.getStatusCode()).isEqualTo(Http.INTERNAL_SERVER_ERROR);
    }
    
    @Test
    public void testNotFoundErrorStatusCode() {
        HttpException he = HttpExceptionFactory.createHttpExceptionFromStatusCode(Http.NOT_FOUND);
        Assertions.assertThat(he).isNotNull().isInstanceOf(NotFoundException.class);
        Assertions.assertThat(he.getStatusCode()).isEqualTo(Http.NOT_FOUND);
    }
    
    @Test
    public void testNotImplementedErrorStatusCode() {
        HttpException he = HttpExceptionFactory.createHttpExceptionFromStatusCode(Http.NOT_IMPLEMENTED);
        Assertions.assertThat(he).isNotNull().isInstanceOf(NotImplementedException.class);
        Assertions.assertThat(he.getStatusCode()).isEqualTo(Http.NOT_IMPLEMENTED);
    }
    
    @Test
    public void testUnauthorizedErrorStatusCode() {
        HttpException he = HttpExceptionFactory.createHttpExceptionFromStatusCode(Http.UNAUTHORIZED);
        Assertions.assertThat(he).isNotNull().isInstanceOf(UnauthorizedException.class);
        Assertions.assertThat(he.getStatusCode()).isEqualTo(Http.UNAUTHORIZED);
    }
    
    @Test
    public void testForbiddenErrorStatusCode() {
        HttpException he = HttpExceptionFactory.createHttpExceptionFromStatusCode(Http.FORBIDDEN);
        Assertions.assertThat(he).isNotNull().isInstanceOf(ForbiddenException.class);
        Assertions.assertThat(he.getStatusCode()).isEqualTo(Http.FORBIDDEN);
    }
    
    @Test
    public void testNotAcceptableErrorStatusCode() {
        HttpException he = HttpExceptionFactory.createHttpExceptionFromStatusCode(Http.NOT_ACCEPTABLE);
        Assertions.assertThat(he).isNotNull().isInstanceOf(NotAcceptableException.class);
        Assertions.assertThat(he.getStatusCode()).isEqualTo(Http.NOT_ACCEPTABLE);
    }
    
}
