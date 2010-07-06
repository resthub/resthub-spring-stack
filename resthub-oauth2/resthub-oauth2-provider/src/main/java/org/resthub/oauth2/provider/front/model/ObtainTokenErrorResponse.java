package org.resthub.oauth2.provider.front.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.resthub.oauth2.provider.exception.ProtocolException;

/**
 * Error response for of the token obtention end-point, as described in the Oauth 2 specification (Section 4.3).
 */
@XmlRootElement
@XmlType(propOrder={"error", "description", "detailsUri"})
public class ObtainTokenErrorResponse implements Serializable {

	private static final long serialVersionUID = -9179219304746945409L;

	// -----------------------------------------------------------------------------------------------------------------
	// Properties

	/**
	 * A single error code.
	 */
	@XmlElement(name="error")
	public String error;

	/**
	 * A human-readable text providing additional information, used to assist in the understanding and resolution of the 
	 * error occurred.
	 */
	@XmlElement(name="error_description")
	public String description;

	/**
	 * A URI identifying a human-readable web page with information about the error, used to provide the end-user with 
	 * additional information about the error.
	 */
	@XmlElement(name="error_uri")
	public String detailsUri;

	// -----------------------------------------------------------------------------------------------------------------
	// Constructors

	/**
	 * Default constructor. Needed for JAX-B.
	 */
	public ObtainTokenErrorResponse() {
		
	} // Constructor.
	
	/**
	 * Parametrized constructor.
	 * 
	 * @param exc Exception source of this error response.
	 */
	public ObtainTokenErrorResponse(ProtocolException exc) {
		error = exc.errorCase.value();
		description = exc.getMessage();
	} // Constructor.
	
	// -----------------------------------------------------------------------------------------------------------------
	// Public inherrited method
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return new StringBuilder("[ObtainTokenErrorResponse] error: ").append(error)
			.append(" description: ").append(description)
			.toString();
	} // toString().

} // class ObtainTokenErrorResponse.
