package org.resthub.web.validation.controller;

import org.apache.commons.io.IOUtils;
import org.fest.assertions.api.Assertions;
import org.resthub.test.AbstractWebTest;
import org.resthub.web.JsonHelper;
import org.resthub.web.Response;
import org.resthub.web.validation.ModelConstraint;
import org.resthub.web.validation.ValidationConstraint;
import org.resthub.web.validation.model.*;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

public class JsonValidationControllerIntegrationTest extends AbstractWebTest {

    public JsonValidationControllerIntegrationTest() {
        super("resthub-web-server, resthub-validation", 9798);
        this.startServerOnce = false;
    }

    private void compareConstraints(ModelConstraint expected, ModelConstraint actual) {
        Map<String, List<ValidationConstraint>> actualConstraints = actual.getConstraints();
        for (Map.Entry<String, List<ValidationConstraint>> entry: expected.getConstraints().entrySet()) {
            Assertions.assertThat(actualConstraints).containsKey(entry.getKey());

            List<ValidationConstraint> actualPropConstraints = actualConstraints.get(entry.getKey());
            comparePropConstraints(entry.getValue(), actualPropConstraints);
        }
    }

    private void comparePropConstraints(List<ValidationConstraint> expected, List<ValidationConstraint> actual) {
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.size()).isEqualTo(expected.size());

        for (ValidationConstraint constraint: expected) {
            Assertions.assertThat(actual).contains(constraint);
            ValidationConstraint actualConstraint = actual.get(actual.indexOf(constraint));
            compareConstraint(constraint, actualConstraint);
        }
    }

    private void compareConstraint(ValidationConstraint expected, ValidationConstraint actual) {
        Assertions.assertThat(actual.getType()).isEqualTo(expected.getType());
        Assertions.assertThat(actual.getMessage()).isEqualTo(expected.getMessage());

        compareAttributes(expected, actual);
    }

    private void compareAttributes(ValidationConstraint expected, ValidationConstraint actual) {
        Map<String, Object> actualAttributes = actual.getAttributes();
        if (expected.getAttributes() == null) {
            Assertions.assertThat(actual.getAttributes()).isNull();
        } else {
            Assertions.assertThat(actualAttributes).isNotNull();
            Assertions.assertThat(actualAttributes.size()).isEqualTo(expected.getAttributes().size());

            for (Map.Entry<String, Object> attrEntry: expected.getAttributes().entrySet()) {
                Assertions.assertThat(actualAttributes).containsKey(attrEntry.getKey());
            }
        }
    }

    @Test
    public void testInheritanceAndCompositionModelWithLocale() {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("validation/aModel.json");
        StringWriter writer = new StringWriter();

        try {
            IOUtils.copy(in, writer);
        } catch (IOException e) {
            Assertions.fail("cannot read file", e);
        }

        String jsonFile = writer.toString();
        ModelConstraint expected = JsonHelper.deserialize(jsonFile, ModelConstraint.class);

        Response response = this.request("api/validation/" + AModel.class.getCanonicalName()).setQueryParameter("locale", "en").get();
        ModelConstraint actual = response.resource(ModelConstraint.class);

        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.getModelRef()).isNotNull().isEqualTo(expected.getModelRef());
        Assertions.assertThat(actual.getConstraints()).isNotNull().isNotEmpty();
        Assertions.assertThat(actual.getConstraints().size()).isEqualTo(expected.getConstraints().size());

        compareConstraints(expected, actual);
    }

    @Test
    public void testInheritanceAndCompositionModelWithoutLocale() {
        Response response = this.request("api/validation/" + AModel.class.getCanonicalName()).get();

        Assertions.assertThat(response.getBody()).contains("constraints");
    }

    @Test
    public void testInheritanceAndCompositionModelWithLocales() {

        InputStream in = this.getClass().getClassLoader().getResourceAsStream("validation/aModel.json");
        StringWriter writer = new StringWriter();

        try {
            IOUtils.copy(in, writer);
        } catch (IOException e) {
            Assertions.fail("cannot read file", e);
        }

        String jsonFile = writer.toString();
        ModelConstraint expected = JsonHelper.deserialize(jsonFile, ModelConstraint.class);

        Response response = this.request("api/validation/" + AModel.class.getCanonicalName()).setQueryParameter("locale", "en-en").get();
        ModelConstraint actual = response.resource(ModelConstraint.class);

        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.getModelRef()).isNotNull().isEqualTo(expected.getModelRef());
        Assertions.assertThat(actual.getConstraints()).isNotNull().isNotEmpty();
        Assertions.assertThat(actual.getConstraints().size()).isEqualTo(expected.getConstraints().size());

        compareConstraints(expected, actual);
    }

    @Test
    public void testNonSupportedClassLevelConstraint() {
        Response response = this.request("api/validation/" + ClassLevelConstraintModel.class.getCanonicalName()).get();

        Assertions.assertThat(response.getBody()).contains("org.resthub.web.validation.model.ClassLevelConstraintModel")
            .contains("\"constraints\" : { }");
    }

    @Test
    public void testNonSupportedInheritedClassLevelConstraint() {
        Response response = this.request("api/validation/" + InheritedClassLevelConstraintModel.class.getCanonicalName()).get();

        Assertions.assertThat(response.getBody()).contains("org.resthub.web.validation.model.InheritedClassLevelConstraintModel")
                .contains("\"type\" : \"Size\"");
    }

    @Test
    public void testAbstractModel() {
        Response response = this.request("api/validation/" + AbstractModel.class.getCanonicalName()).get();

        Assertions.assertThat(response.getBody()).contains("org.resthub.web.validation.model.AbstractModel")
                .contains("\"constraints\" : { }");
    }

    @Test
    public void testNoConstraintsModel() {
        Response response = this.request("api/validation/" + NoConstraintsModel.class.getCanonicalName()).get();

        Assertions.assertThat(response.getBody()).contains("org.resthub.web.validation.model.NoConstraintsModel")
                .contains("\"constraints\" : { }");
    }
}
