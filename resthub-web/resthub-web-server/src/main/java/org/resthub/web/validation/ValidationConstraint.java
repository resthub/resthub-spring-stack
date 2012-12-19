package org.resthub.web.validation;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidationConstraint {

    private String type;
    private String message;
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

    public Object addAttribute(String attrKey, Object attribute) {

        if (this.attributes == null) {
            this.attributes = new HashMap<String, Object>();
        }

        return this.attributes.put(attrKey, attribute);
    }
}
