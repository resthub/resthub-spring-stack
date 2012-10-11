package org.resthub.web.exception;

import org.resthub.web.Http;

public class ClientExceptionFactory {
        
    public static ClientException createHttpExceptionFromStatusCode(int statusCode) {
        return createHttpExceptionFromStatusCode(statusCode, "");
    }
        
    public static ClientException createHttpExceptionFromStatusCode(int statusCode, String message) {
        switch(statusCode) {
            case Http.BAD_REQUEST:
                return new BadRequestClientException(message);
            case Http.CONFLICT:
                return new ConflictClientException(message);
            case Http.INTERNAL_SERVER_ERROR:
                return new InternalServerErrorClientException(message);
            case Http.NOT_FOUND:
                return new NotFoundClientException(message);
            case Http.NOT_IMPLEMENTED:
                return new NotImplementedClientException(message);
            case Http.UNAUTHORIZED:
                return new UnauthorizedClientException(message);
            case Http.FORBIDDEN:
                return new ForbiddenClientException(message);
            case Http.NOT_ACCEPTABLE:
                return new NotAcceptableClientException(message);
             default:
                 if((statusCode >= 400) && (statusCode <= 599)){
                     return new ClientException(statusCode, message);
                 } else {
                     throw new IllegalArgumentException("Status code " + statusCode + " is not an HTTP error code");
                 }
            }
      }   
}
