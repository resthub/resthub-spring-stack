package org.resthub.web.validation.controller;

import org.apache.commons.io.IOUtils;
import org.fest.assertions.api.Assertions;
import org.resthub.test.AbstractWebTest;
import org.resthub.web.Response;
import org.resthub.web.validation.model.AModel;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class ValidationControllerIntegrationTest extends AbstractWebTest {

    public ValidationControllerIntegrationTest() {
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

        Assertions.assertThat(response.getBody()).isEqualTo(jsonFile);
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

        Assertions.assertThat(response.getBody()).isEqualTo(jsonFile);
    }
}
