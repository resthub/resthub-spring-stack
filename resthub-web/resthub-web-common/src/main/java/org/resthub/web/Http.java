package org.resthub.web;

/**
 * HTTP status code and header constant definitions
 */
public class Http {

    /** Accept header name, define response content type **/
    public final static String ACCEPT = "Accept";

    /** Content type header name, define request content type **/
    public final static String CONTENT_TYPE = "Content-Type";

    /** Authorization header name **/
    public final static String AUTHORIZATION = "Authorization";

    /** JSON content type value, can be used for Accept or Content-type header **/
    public final static String JSON = "application/json";

    /** XML content type value, can be used for Accept or Content-type header **/
    public final static String XML = "application/xml";

    /** Form content type value, can be used for Accept or Content-type header **/
    public final static String FORM = "application/x-www-form-urlencoded";

    /** OK HTTP status code value **/
    public final static int OK = 200;

    /** CREATED HTTP status code value **/
    public final static int CREATED = 201;

    /** OK NO_CONTENT status code value **/
    public final static int NO_CONTENT = 204;

    /** MOVED_PERMANENTLY HTTP status code value **/
    public final static int MOVED_PERMANENTLY = 301;

    /** FOUND HTTP status code value **/
    public final static int FOUND = 302;

    /** NOT_MODIFIED HTTP status code value **/
    public final static int NOT_MODIFIED = 304;

    /** BAD_REQUEST HTTP status code value **/
    public final static int BAD_REQUEST = 400;

    /** UNAUTHORIZED HTTP status code value **/
    public final static int UNAUTHORIZED = 401;

    /** FORBIDDEN HTTP status code value **/
    public final static int FORBIDDEN = 403;

    /** NOT_FOUND HTTP status code value **/
    public final static int NOT_FOUND = 404;

    /** NOT_ACCEPTABLE HTTP status code value **/
    public final static int NOT_ACCEPTABLE = 406;

    /** CONFLICT HTTP status code value **/
    public final static int CONFLICT = 409;

    /** INTERNAL_SERVER_ERROR HTTP status code value **/
    public final static int INTERNAL_SERVER_ERROR = 500;
    
    /** NOT_IMPLEMENTED HTTP status code value **/
    public final static int NOT_IMPLEMENTED = 501;
}
