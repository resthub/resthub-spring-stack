package org.resthub.web.exception;

import org.resthub.web.Http;

public class HttpExceptionFactory {
        
    public static HttpException createHttpExceptionFromStatusCode(int statusCode) {
        return createHttpExceptionFromStatusCode(statusCode, "");
    }
        
    public static HttpException createHttpExceptionFromStatusCode(int statusCode, String message) {
        switch(statusCode) {
            case Http.BAD_REQUEST:
                return new BadRequestException(message);
            case Http.CONFLICT:
                return new ConflictException(message);
            case Http.INTERNAL_SERVER_ERROR:
                return new InternalServerErrorException(message);
            case Http.NOT_FOUND:
                return new NotFoundException(message);
            case Http.NOT_IMPLEMENTED:
                return new NotImplementedException(message);
            case Http.UNAUTHORIZED:
                return new UnauthorizedException(message);
            case Http.FORBIDDEN:
                return new ForbiddenException(message);
            case Http.NOT_ACCEPTABLE:
                return new NotAcceptableException(message);
             default:
                 if((statusCode >= 400) && (statusCode <= 499)){
                     return new HttpClientErrorException(message);
                 } else if ((statusCode >= 500) && (statusCode <= 599)){
                     return new HttpServerErrorException(message);
                 } else {
                     throw new IllegalArgumentException("Status code " + statusCode + " is not an HTTP error code");
                 }
            }
      }   
}
