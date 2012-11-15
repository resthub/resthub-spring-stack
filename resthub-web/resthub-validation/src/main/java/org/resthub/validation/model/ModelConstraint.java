package org.resthub.validation.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonPropertyOrder(value = {"model", "constraints"})
public class ModelConstraint {

    private String modelRef;
    private Map<String, List<ValidationConstraint>> constraints;

    public ModelConstraint(String modelRef) {
        this.modelRef = modelRef;
    }

    @JsonProperty(value = "model")
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
}
