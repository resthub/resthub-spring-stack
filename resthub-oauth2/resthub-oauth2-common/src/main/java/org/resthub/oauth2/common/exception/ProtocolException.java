package org.resthub.oauth2.common.exception;

import javax.ws.rs.core.Response.Status;

/**
 * OAuth v2 protocl exception, used in service layer to indicates protocol errors.
 */
public class ProtocolException extends RuntimeException {

	private static final long serialVersionUID = -4289361554763838764L;

	// -----------------------------------------------------------------------------------------------------------------
	// Public constants

	/**
	 * Different protocol errors (value are described by the protocol).
	 */
	public enum Error {
		/**
		 * Used when responding to a request that do not have access token.
		 * No Error code or explanation must be returned.
		 */
		UNAUTHORIZED_REQUEST(null, Status.UNAUTHORIZED),

		/**
		 * The access token provided is invalid. Used when receiving an expired token which cannot be refreshed to 
		 * indicate to the client that a new authorization is necessary.
		 */
		INVALID_TOKEN("invalid_token", Status.UNAUTHORIZED),

		/**
		 * The access token provided has expired. Used only this error code when the client is expected to be able
         * to handle the response and request a new access token using the refresh token issued with the expired access 
         * token.
		 */
		EXPIRED_TOKEN("expired_token", Status.UNAUTHORIZED),

		/**
		 * The request is missing a required parameter, includes an unknown parameter or parameter value, 
		 * repeats a parameter, includes multiple credentials, utilizes more than one mechanism for 
		 * authenticating the client, or is otherwise malformed.
		 */
		INVALID_REQUEST("invalid_request", Status.BAD_REQUEST),

		/**
		 * The access grant included - its type or another attribute - is not supported by the authorization server.
		 */
		UNSUPPORTED_GRANT_TYPE("unsupported_grant_type", Status.BAD_REQUEST),

		/**
		 * The client identifier provided is invalid, the client failed to authenticate, or the client provided multiple
		 * client credentials. 
		 */
		INVALID_CLIENT("invalid_client", Status.BAD_REQUEST),
		
		/**
		 * The requested scope is invalid, unknown, malformed, or exceeds the previously granted scope. 
		 */
		INVALID_SCOPE("invalid_scope", Status.BAD_REQUEST),

		/**
		 *  The request requires higher privileges than provided by the access token.  
		 */
		INSUFFICIENT_SCOPE("insufficient_scope", Status.FORBIDDEN);

		/**
		 * String value for this error case.
		 */
		private String innerValue;

		/**
		 * Http Status for this error case.
		 */
		private Status httpStatus;

		/**
		 * Parametrized constructor.
		 * 
		 * @param value String value for this error case.
		 */
		private Error(String value, Status status) {
			innerValue = value;
			httpStatus = status;
		} // Constructor.
		
		/**
		 * Returns the value for this error case.
		 * @return Protocol-defined value of the error case.
		 */
		public String value(){
			return innerValue;
		} // value().

		/**
		 * Returns the HTTP status for this error case.
		 * @return HTTP Status of the error case.
		 */
		public Status status(){
			return httpStatus;
		} // status().

	} // enum Type
	
	// -----------------------------------------------------------------------------------------------------------------
	// Public attributes

	/**
	 * Error case.
	 */
	public Error errorCase;
	
	// -----------------------------------------------------------------------------------------------------------------
	// Constructor
	
	/**
	 * Constructor.
	 * 
	 * @param errorCase Detailed error case.
	 */
	public ProtocolException(Error errorCase) {
		super((String)null);
		this.errorCase = errorCase;
	} // ProtocolException().

	/**
	 * Constructor.
	 * 
	 * @param errorCase Detailed error case.
	 * @param message Explanation message.
	 */
	public ProtocolException(Error errorCase, String message) {
		super(message);
		this.errorCase = errorCase;
	} // ProtocolException().

	/**
	 * Constructor.
	 * 
	 * @param errorCase Detailed error case.
	 * @param message Explanation message.
	 * @param cause Exception root cause.
	 */
	public ProtocolException(Error errorCase, String message, Throwable cause) {
		super(message, cause);
		this.errorCase = errorCase;
	} // ProtocolException().
	
} // class ProtocolException
