package org.resthub.oauth2.provider.exception;

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
	public enum Type {
		/**
		 * The request is missing a required parameter, includes an unknown parameter or parameter value, 
		 * repeats a parameter, includes multiple credentials, utilizes more than one mechanism for 
		 * authenticating the client, or is otherwise malformed.
		 */
		INVALID_REQUEST("invalid-request"),

		/**
		 * The access grant included - its type or another attribute - is not supported by the authorization server.
		 */
		UNSUPPORTED_GRANT_TYPE("unsupported-grant-type"),

		/**
		 * The client identifier provided is invalid, the client failed to authenticate, or the client provided multiple
		 * client credentials. 
		 */
		INVALID_CLIENT_CREDENTIALS("invalid-client-credentials"),
		
		/**
		 * The requested scope is invalid, unknown, malformed, or exceeds the previously granted scope. 
		 */
		INVALID_SCOPE("invalid-scope");
		
		/**
		 * String value for this error case.
		 */
		private String innerValue;
		
		/**
		 * Parametrized constructor.
		 * 
		 * @param value String value for this error case.
		 */
		private Type(String value) {
			innerValue = value;
		} // Constructor.
		
		/**
		 * Returns the value for this error case.
		 * @return Protocol-defined value of the error case.
		 */
		public String value(){
			return innerValue;
		} // value().
		
	} // enum Type
	
	// -----------------------------------------------------------------------------------------------------------------
	// Public attributes

	/**
	 * Error case.
	 */
	public Type errorCase;
	
	// -----------------------------------------------------------------------------------------------------------------
	// Constructor
	
	/**
	 * Constructor.
	 * 
	 * @param errorCase Detailed error case.
	 * @param message Explanation message.
	 */
	public ProtocolException(Type errorCase, String message) {
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
	public ProtocolException(Type errorCase, String message, Throwable cause) {
		super(message, cause);
		this.errorCase = errorCase;
	} // ProtocolException().
	
} // class ProtocolException
