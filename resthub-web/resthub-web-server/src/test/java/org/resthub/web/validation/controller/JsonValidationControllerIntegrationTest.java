package org.resthub.web.validation.controller;

import org.apache.commons.io.IOUtils;
import org.fest.assertions.api.Assertions;
import org.resthub.test.AbstractWebTest;
import org.resthub.web.Response;
import org.resthub.web.validation.ModelConstraint;
import org.resthub.web.validation.model.*;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class JsonValidationControllerIntegrationTest extends AbstractWebTest {

    public JsonValidationControllerIntegrationTest() {
        super("resthub-web-server, resthub-validation", 9798);
        this.startServerOnce = false;
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
        Response response = this.request("api/validation/" + AModel.class.getCanonicalName()).setQueryParameter("locale", "en").get();

        Assertions.assertThat(response.getBody().replaceAll("(\\r|\\n)", "")).isEqualTo(jsonFile.replaceAll("(\\r|\\n)", ""));
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

        Response response = this.request("api/validation/" + AModel.class.getCanonicalName()).setQueryParameter("locale", "en-en").get();

        Assertions.assertThat(response.getBody().replaceAll("(\\r|\\n)", "")).isEqualTo(jsonFile.replaceAll("(\\r|\\n)", ""));
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
