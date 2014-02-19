package org.resthub.web.validation;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Object representation of validation constraints.
 *
 * Instances of this class can be easily serialized in JSON or XML format thanks to dedicated
 * Jackson configuration.
 *
 * <br/><br/>
 * <b>Sample JSON serialization:</b>
 * <br/><br/>
 * <pre>
 *          "lastName": [{
 *              "type": "NotBlank",
 *              "message": "may not be empty"
 *          }],
 *          "email": [{
 *              "type": "NotNull",
 *              "message": "may not be null"
 *           }, {
 *              "type": "Email",
 *              "message": "not a well-formed email address",
 *              "flags": [],
 *              "regexp": ".*"
 *          }],
 * </pre>
 *
 * <br/><br/>
 * <b>Sample XML serialization:</b>
 * <br/><br/>
 * <pre>
 *          < lastName>
 *              < type>NotBlank</type>
 *              < message>may not be empty</message>
 *          < /lastName>
 *          < email>
 *              < type>NotNull</type>
 *              < message>may not be null</message>
 *          < /email>
 *          < email>
 *              < type>Email</type>
 *              < message>not a well-formed email address</message>
 *              < regexp>.*</regexp>
 *          < /email>
 * </pre>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidationConstraint {

    /**
     * Constraint type
     */
    private String type;

    /**
     * Associated constraint message
     */
    private String message;

    /**
     * Optional constraints attributes
     */
    private Map<String, Object> attributes;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @JsonAnyGetter
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @JsonAnySetter
    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    /**
     * Add attribute to the current constraint definition.
     *
     * @param attrKey attribute key identifier
     * @param attribute attribute value
     *
     * @return the previous value associated with <tt>attrKey</tt>, or
     *         <tt>null</tt> if there was no mapping for <tt>attrKey</tt>.
     */
    public Object addAttribute(String attrKey, Object attribute) {

        if (this.attributes == null) {
            this.attributes = new HashMap<String, Object>();
        }

        return this.attributes.put(attrKey, attribute);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ValidationConstraint)) return false;

        ValidationConstraint that = (ValidationConstraint) o;

        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }
}
