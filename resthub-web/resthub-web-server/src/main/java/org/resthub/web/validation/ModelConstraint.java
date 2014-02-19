package org.resthub.web.validation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Object representation of validation constraints defined on a model domain object (POJO).
 *
 * Instances of this class can be easily serialized in JSON or XML format thanks to dedicated
 * Jackson configuration.
 *
 * <br/><br/>
 * <b>Sample JSON serialization:</b>
 * <br/><br/>
 * <pre>
 * {
 *   "model": "org.resthub.validation.model.User",
 *      "constraints": {
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
 *          "login": [{
 *              "type": "NotNull",
 *              "message": "may not be null"
 *          }, {
 *              "type": "Length",
 *              "message": "length must be between 8 and 2147483647",
 *              "min": 8,
 *              "max": 2147483647
 *          }],
 *          "firstName": [{
 *              "type": "NotBlank",
 *              "message": "may not be empty"
 *          }]
 *      }
 * }
 * </pre>
 *
 * <br/><br/>
 * <b>Sample XML serialization:</b>
 * <br/><br/>
 * <pre>
 * < ModelConstraint>
 *      < model>org.resthub.validation.model.User</model>
 *      < constraints>
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
 *          < login>
 *              < type>NotNull</type>
 *              < message>may not be null</message>
 *          < /login>
 *          < login>
 *              < type>Length</type>
 *              < message>length must be between 8 and 2147483647</message>
 *              < min>8</min>
 *              < max>2147483647</max>
 *          < /login>
 *          < firstName>
 *              < type>NotBlank</type>
 *              < message>may not be empty</message>
 *          < /firstName>
 *      < /constraints>
 * < /ModelConstraint>
 * </pre>
 *
 * @see org.resthub.web.validation.ValidationConstraint
 */
@JsonPropertyOrder(value = {"model", "constraints"})
public class ModelConstraint {

    /**
     * Model class name
     */
    private String modelRef;

    /**
     * Map of all validation constraints on each model property
     *
     * @see org.resthub.web.validation.ValidationConstraint
     */
    private Map<String, List<ValidationConstraint>> constraints;

    @JsonCreator
    public ModelConstraint(@JsonProperty("model") String modelRef) {
        this.modelRef = modelRef;
        this.constraints = new HashMap<String, List<ValidationConstraint>>();
    }

    @JsonProperty("model")
    public String getModelRef() {
        return modelRef;
    }

    public void setModelRef(String modelRef) {
        this.modelRef = modelRef;
    }

    public Map<String, List<ValidationConstraint>> getConstraints() {
        return constraints;
    }

    public void setConstraints(Map<String, List<ValidationConstraint>> constraints) {
        this.constraints = constraints;
    }

    /**
     * Add a {@link ValidationConstraint} for a given property for the current represented model
     *
     * @param property model property holding the constraint
     * @param constraint {@link ValidationConstraint} instance to add to model constraints definitions
     *
     * @return the updated list of {@link ValidationConstraint} for the given <tt>property</tt> parameter.
     */
    public List<ValidationConstraint> addConstraint(String property, ValidationConstraint constraint) {

        if (null == this.constraints) {
            this.constraints = new HashMap<String, List<ValidationConstraint>>();
        }

        List<ValidationConstraint> propertyConstraints = this.constraints.get(property);
        if (null == propertyConstraints) {
            propertyConstraints = new ArrayList<ValidationConstraint>();
        }
        propertyConstraints.add(constraint);

        return this.constraints.put(property, propertyConstraints);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ModelConstraint)) return false;

        ModelConstraint that = (ModelConstraint) o;

        if (modelRef != null ? !modelRef.equals(that.modelRef) : that.modelRef != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return modelRef != null ? modelRef.hashCode() : 0;
    }
}
