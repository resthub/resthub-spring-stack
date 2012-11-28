package org.resthub.web.validation.service;

import org.fest.assertions.api.Assertions;
import org.resthub.test.AbstractTest;
import org.resthub.web.validation.ModelConstraint;
import org.resthub.web.validation.ValidationService;
import org.resthub.web.validation.model.*;
import org.springframework.test.context.ActiveProfiles;
import org.testng.annotations.Test;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Locale;

@ActiveProfiles({"resthub-validation", "resthub-web-server", "resthub-jpa"})
public class ValidationServiceTest extends AbstractTest {

    @Inject
    @Named("validationService")
    private ValidationService validationService;

    @Test(expectedExceptions = {ClassNotFoundException.class})
    public void testNotNullValidationModelFromUnknownClassName() throws ClassNotFoundException {
        this.validationService.getConstraintsForClassName("unknownClassName");
    }

    @Test
    public void testNotNullValidationModelFromExistingClassName() {
        ModelConstraint modelConstraint = null;

        try {
            modelConstraint =
                    this.validationService.getConstraintsForClassName("org.resthub.web.validation.model.NotNullValidationModel");
        } catch (ClassNotFoundException e) {
            Assertions.fail("Error: cannot find class !", e);
        }

        this.validateNotNullValidationModel(modelConstraint);
    }

    @Test
    public void testNotNullValidationModelFromExistingClass() {
        ModelConstraint modelConstraint =
                this.validationService.getConstraintsForClass(NotNullValidationModel.class);
        this.validateNotNullValidationModel(modelConstraint);
    }

    @Test
    public void testNotNullValidationModelFromExistingClassAndLocales() {
        ModelConstraint modelConstraint =
                this.validationService.getConstraintsForClass(NotNullValidationModel.class, new Locale("fr"));
        this.validateNotNullValidationModel(modelConstraint);
        Assertions.assertThat(modelConstraint.getConstraints().get("field").get(0).getMessage()).isEqualTo("ne peut pas être nul");

        modelConstraint =
                this.validationService.getConstraintsForClass(NotNullValidationModel.class, new Locale("en", "us"));
        this.validateNotNullValidationModel(modelConstraint);
        Assertions.assertThat(modelConstraint.getConstraints().get("field").get(0).getMessage()).isEqualTo("may not be null");
    }

    private void validateNotNullValidationModel(ModelConstraint modelConstraint) {
        Assertions.assertThat(modelConstraint).isNotNull();
        Assertions.assertThat(modelConstraint.getModelRef()).isNotNull().isEqualTo("org.resthub.web.validation.model.NotNullValidationModel");
        Assertions.assertThat(modelConstraint.getConstraints()).isNotNull().isNotEmpty().hasSize(1);
        Assertions.assertThat(modelConstraint.getConstraints().containsKey("field"));
        Assertions.assertThat(modelConstraint.getConstraints().get("field")).isNotNull().isNotEmpty().hasSize(1);
        Assertions.assertThat(modelConstraint.getConstraints().get("field").get(0).getType()).isEqualTo("NotNull");
        Assertions.assertThat(modelConstraint.getConstraints().get("field").get(0).getMessage()).isNotNull().isNotEmpty();
        Assertions.assertThat(modelConstraint.getConstraints().get("field").get(0).getAttributes()).isNotNull().isEmpty();
    }

    @Test
    public void testCompleteValidationModel() {
        ModelConstraint modelConstraint =
                this.validationService.getConstraintsForClass(CompleteValidationModel.class);

        Assertions.assertThat(modelConstraint).isNotNull();
        Assertions.assertThat(modelConstraint.getModelRef()).isNotNull().isEqualTo("org.resthub.web.validation.model.CompleteValidationModel");
        Assertions.assertThat(modelConstraint.getConstraints()).isNotNull().isNotEmpty().hasSize(20);
        Assertions.assertThat(modelConstraint.getConstraints()).containsKey("assertFalse").containsKey("digits").containsKey("ANull").containsKey("size");

        Assertions.assertThat(modelConstraint.getConstraints().get("digits")).isNotNull().isNotEmpty().hasSize(1);
        Assertions.assertThat(modelConstraint.getConstraints().get("digits").get(0).getType()).isEqualTo("Digits");
        Assertions.assertThat(modelConstraint.getConstraints().get("digits").get(0).getMessage()).isNotNull().isNotEmpty();
        Assertions.assertThat(modelConstraint.getConstraints().get("digits").get(0).getAttributes()).isNotNull().isNotEmpty().hasSize(2);
        Assertions.assertThat(modelConstraint.getConstraints().get("digits").get(0).getAttributes()).containsKey("integer").containsKey("fraction");
        Assertions.assertThat(modelConstraint.getConstraints().get("digits").get(0).getAttributes().get("integer")).isEqualTo(2);
        Assertions.assertThat(modelConstraint.getConstraints().get("digits").get(0).getAttributes().get("fraction")).isEqualTo(3);

        Assertions.assertThat(modelConstraint.getConstraints().get("size")).isNotNull().isNotEmpty().hasSize(1);
        Assertions.assertThat(modelConstraint.getConstraints().get("size").get(0).getType()).isEqualTo("Size");
        Assertions.assertThat(modelConstraint.getConstraints().get("size").get(0).getMessage()).isNotNull().isNotEmpty();
        Assertions.assertThat(modelConstraint.getConstraints().get("size").get(0).getAttributes()).isNotNull().isNotEmpty().hasSize(2);
        Assertions.assertThat(modelConstraint.getConstraints().get("size").get(0).getAttributes()).containsKey("min").containsKey("max");
        Assertions.assertThat(modelConstraint.getConstraints().get("size").get(0).getAttributes().get("min")).isEqualTo(1);
        Assertions.assertThat(modelConstraint.getConstraints().get("size").get(0).getAttributes().get("max")).isEqualTo(10);

        Assertions.assertThat(modelConstraint.getConstraints()).containsKey("creditCardNumber").containsKey("email").containsKey("url").containsKey("notBlank").containsKey("notEmpty").containsKey("range").containsKey("length");

    }

    @Test
    public void testCombinedValidationModel() {
        ModelConstraint modelConstraint =
                this.validationService.getConstraintsForClass(CombinedValidationModel.class);

        Assertions.assertThat(modelConstraint).isNotNull();
        Assertions.assertThat(modelConstraint.getModelRef()).isNotNull().isEqualTo("org.resthub.web.validation.model.CombinedValidationModel");
        Assertions.assertThat(modelConstraint.getConstraints()).isNotNull().isNotEmpty().hasSize(2);
        Assertions.assertThat(modelConstraint.getConstraints()).containsKey("login").containsKey("password");

        Assertions.assertThat(modelConstraint.getConstraints().get("login")).isNotNull().isNotEmpty().hasSize(2);
        Assertions.assertThat(modelConstraint.getConstraints().get("login").get(0).getType()).isEqualTo("NotNull");
        Assertions.assertThat(modelConstraint.getConstraints().get("login").get(0).getMessage()).isNotNull().isNotEmpty();
        Assertions.assertThat(modelConstraint.getConstraints().get("login").get(0).getAttributes()).isNotNull().isEmpty();
        Assertions.assertThat(modelConstraint.getConstraints().get("login").get(1).getType()).isEqualTo("Pattern");
        Assertions.assertThat(modelConstraint.getConstraints().get("login").get(1).getMessage()).isNotNull().isNotEmpty();
        Assertions.assertThat(modelConstraint.getConstraints().get("login").get(1).getAttributes()).isNotNull().isNotEmpty().hasSize(2);
        Assertions.assertThat(modelConstraint.getConstraints().get("login").get(1).getAttributes()).containsKey("flags").containsKey("regexp");
        Assertions.assertThat(modelConstraint.getConstraints().get("login").get(1).getAttributes().get("regexp")).isNotNull();
        Assertions.assertThat(modelConstraint.getConstraints().get("login").get(1).getAttributes().get("flags")).isNotNull();

        Assertions.assertThat(modelConstraint.getConstraints().get("password")).isNotNull().isNotEmpty().hasSize(3);
        Assertions.assertThat(modelConstraint.getConstraints().get("password").get(0).getType()).isEqualTo("NotNull");
        Assertions.assertThat(modelConstraint.getConstraints().get("password").get(0).getMessage()).isNotNull().isNotEmpty();
        Assertions.assertThat(modelConstraint.getConstraints().get("password").get(0).getAttributes()).isNotNull().isEmpty();
        Assertions.assertThat(modelConstraint.getConstraints().get("password").get(1).getType()).isEqualTo("Size");
        Assertions.assertThat(modelConstraint.getConstraints().get("password").get(1).getMessage()).isNotNull().isNotEmpty();
        Assertions.assertThat(modelConstraint.getConstraints().get("password").get(1).getAttributes()).isNotNull().isNotEmpty().hasSize(2);
        Assertions.assertThat(modelConstraint.getConstraints().get("password").get(1).getAttributes()).containsKey("min").containsKey("max");
        Assertions.assertThat(modelConstraint.getConstraints().get("password").get(1).getAttributes().get("min")).isNotNull();
        Assertions.assertThat(modelConstraint.getConstraints().get("password").get(1).getAttributes().get("max")).isNotNull();
        Assertions.assertThat(modelConstraint.getConstraints().get("password").get(2).getType()).isEqualTo("Pattern");
        Assertions.assertThat(modelConstraint.getConstraints().get("password").get(2).getMessage()).isNotNull().isNotEmpty();
        Assertions.assertThat(modelConstraint.getConstraints().get("password").get(2).getAttributes()).isNotNull().isNotEmpty().hasSize(2);
        Assertions.assertThat(modelConstraint.getConstraints().get("password").get(2).getAttributes()).containsKey("flags").containsKey("regexp");
        Assertions.assertThat(modelConstraint.getConstraints().get("password").get(2).getAttributes().get("regexp")).isNotNull();
        Assertions.assertThat(modelConstraint.getConstraints().get("password").get(2).getAttributes().get("flags")).isNotNull();
    }

    @Test
    public void testInheritanceAndCompositionValidationModel() {
        ModelConstraint modelConstraint =
                this.validationService.getConstraintsForClass(AModel.class);

        Assertions.assertThat(modelConstraint).isNotNull();
        Assertions.assertThat(modelConstraint.getModelRef()).isNotNull().isEqualTo("org.resthub.web.validation.model.AModel");
        Assertions.assertThat(modelConstraint.getConstraints()).isNotNull().isNotEmpty().hasSize(3);
        Assertions.assertThat(modelConstraint.getConstraints()).containsKey("field1").containsKey("field2").containsKey("field5");

        Assertions.assertThat(modelConstraint.getConstraints().get("field1")).isNotNull().isNotEmpty().hasSize(2);
        Assertions.assertThat(modelConstraint.getConstraints().get("field1").get(0).getType()).isEqualTo("NotNull");
        Assertions.assertThat(modelConstraint.getConstraints().get("field1").get(1).getType()).isEqualTo("Size");

        Assertions.assertThat(modelConstraint.getConstraints().get("field2")).isNotNull().isNotEmpty().hasSize(1);
        Assertions.assertThat(modelConstraint.getConstraints().get("field2").get(0).getType()).isEqualTo("Pattern");

        Assertions.assertThat(modelConstraint.getConstraints().get("field5")).isNotNull().isNotEmpty().hasSize(2);
        Assertions.assertThat(modelConstraint.getConstraints().get("field5").get(0).getType()).isEqualTo("NotNull");
        Assertions.assertThat(modelConstraint.getConstraints().get("field5").get(1).getType()).isEqualTo("Valid");
        Assertions.assertThat(modelConstraint.getConstraints().get("field5").get(1).getAttributes()).isNotNull().isNotEmpty().hasSize(1);
        Assertions.assertThat(modelConstraint.getConstraints().get("field5").get(1).getAttributes()).containsKey("model");
        Assertions.assertThat(modelConstraint.getConstraints().get("field5").get(1).getAttributes().get("model")).isNotNull().isEqualTo("org.resthub.web.validation.model.BModel");

        modelConstraint =
                this.validationService.getConstraintsForClass(BModel.class);

        Assertions.assertThat(modelConstraint).isNotNull();
        Assertions.assertThat(modelConstraint.getModelRef()).isNotNull().isEqualTo("org.resthub.web.validation.model.BModel");
        Assertions.assertThat(modelConstraint.getConstraints()).isNotNull().isNotEmpty().hasSize(2);
        Assertions.assertThat(modelConstraint.getConstraints()).containsKey("field3").containsKey("field4");

        Assertions.assertThat(modelConstraint.getConstraints().get("field3")).isNotNull().isNotEmpty().hasSize(1);
        Assertions.assertThat(modelConstraint.getConstraints().get("field3").get(0).getType()).isEqualTo("NotNull");

        Assertions.assertThat(modelConstraint.getConstraints().get("field4")).isNotNull().isNotEmpty().hasSize(1);
        Assertions.assertThat(modelConstraint.getConstraints().get("field4").get(0).getType()).isEqualTo("Size");

        modelConstraint =
                this.validationService.getConstraintsForClass(AbstractModel.class);
        Assertions.assertThat(modelConstraint).isNull();
    }

    @Test
    public void testNotNullValidationModelFromExistingClassKeyOnly() {
        ModelConstraint modelConstraint =
                this.validationService.getConstraintsForClass(NotNullValidationModel.class, true);
        this.validateNotNullValidationModel(modelConstraint);
        Assertions.assertThat(modelConstraint.getConstraints().get("field").get(0).getMessage()).isEqualTo("{javax.validation.constraints.NotNull.message}");
    }
}
