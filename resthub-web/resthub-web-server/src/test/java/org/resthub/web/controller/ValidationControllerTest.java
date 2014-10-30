package org.resthub.web.controller;

import org.fest.assertions.api.Assertions;
import org.resthub.common.exception.NotFoundException;
import org.resthub.web.controller.ValidationController;
import org.resthub.web.validation.ValidationService;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Locale;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class ValidationControllerTest {

    private ValidationService validationService = mock(ValidationService.class);
    private ValidationController validationController;

    @BeforeClass
    public void setup() {
        this.validationController = new ValidationController();
        this.validationController.setService(this.validationService);
    }

    @BeforeMethod
    public void setupTest() throws ClassNotFoundException{
        reset(this.validationService);

        when(this.validationService.getConstraintsForClassName(eq("unknownClassName"), any(Locale.class))).thenThrow(new ClassNotFoundException());
    }

    @Test
    public void testEmptyLocale() {
        String loc = "";
        Locale locale = this.validationController.parseLocale(loc);

        Assertions.assertThat(locale).isNotNull();
        Assertions.assertThat(locale.getLanguage()).isNotNull().isEqualTo(loc);
        Assertions.assertThat(locale.getCountry()).isNotNull().isEqualTo("");
        Assertions.assertThat(locale.getVariant()).isNotNull().isEqualTo("");
    }

    @Test
    public void testLanguageLocale() {
        String loc = "fr";
        Locale locale = this.validationController.parseLocale(loc);

        Assertions.assertThat(locale).isNotNull();
        Assertions.assertThat(locale.getLanguage()).isNotNull().isEqualTo(loc);
        Assertions.assertThat(locale.getCountry()).isNotNull().isEqualTo("");
        Assertions.assertThat(locale.getVariant()).isNotNull().isEqualTo("");
    }

    @Test
    public void testTrailingLanguageLocale() {
        String loc = "fr-";
        Locale locale = this.validationController.parseLocale(loc);

        Assertions.assertThat(locale).isNotNull();
        Assertions.assertThat(locale.getLanguage()).isNotNull().isEqualTo("fr");
        Assertions.assertThat(locale.getCountry()).isNotNull().isEqualTo("");
        Assertions.assertThat(locale.getVariant()).isNotNull().isEqualTo("");
    }

    @Test
    public void testLanguageCountryLocale() {
        String loc = "fr-FR";
        Locale locale = this.validationController.parseLocale(loc);

        Assertions.assertThat(locale).isNotNull();
        Assertions.assertThat(locale.getLanguage()).isNotNull().isEqualTo("fr");
        Assertions.assertThat(locale.getCountry()).isNotNull().isEqualTo("FR");
        Assertions.assertThat(locale.getVariant()).isNotNull().isEqualTo("");
    }

    @Test
    public void testTrailingLanguageCountryLocale() {
        String loc = "fr-FR-";
        Locale locale = this.validationController.parseLocale(loc);

        Assertions.assertThat(locale).isNotNull();
        Assertions.assertThat(locale.getLanguage()).isNotNull().isEqualTo("fr");
        Assertions.assertThat(locale.getCountry()).isNotNull().isEqualTo("FR");
        Assertions.assertThat(locale.getVariant()).isNotNull().isEqualTo("");
    }

    @Test
    public void testLanguageCountryVariantLocale() {
        String loc = "fr-FR-FR";
        Locale locale = this.validationController.parseLocale(loc);

        Assertions.assertThat(locale).isNotNull();
        Assertions.assertThat(locale.getLanguage()).isNotNull().isEqualTo("fr");
        Assertions.assertThat(locale.getCountry()).isNotNull().isEqualTo("FR");
        Assertions.assertThat(locale.getVariant()).isNotNull().isEqualTo("FR");
    }

    @Test
    public void testTrailingLanguageCountryVariantLocale() {
        String loc = "fr-FR-FR-";
        Locale locale = this.validationController.parseLocale(loc);

        Assertions.assertThat(locale).isNotNull();
        Assertions.assertThat(locale.getLanguage()).isNotNull().isEqualTo("fr");
        Assertions.assertThat(locale.getCountry()).isNotNull().isEqualTo("FR");
        Assertions.assertThat(locale.getVariant()).isNotNull().isEqualTo("FR");
    }

    @Test
    public void testGetConstraints() throws ClassNotFoundException {
        this.validationController.getConstraintsForClassName("knownClassName", "fr");

        verify(this.validationService).getConstraintsForClassName(eq("knownClassName"), any(Locale.class));
    }

    @Test
    public void testGetConstraintsNoLocale() throws ClassNotFoundException {
        this.validationController.getConstraintsForClassName("knownClassName", null);

        verify(this.validationService).getConstraintsForClassName(eq("knownClassName"), isNull(Locale.class));
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void testGetConstraintsUnknownClass() {
        this.validationController.getConstraintsForClassName("unknownClassName", "fr");
    }

}
