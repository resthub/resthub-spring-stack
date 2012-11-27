package org.resthub.web.controller;

import org.resthub.common.exception.NotFoundException;
import org.resthub.web.exception.NotFoundClientException;
import org.resthub.web.validation.ModelConstraint;
import org.resthub.web.validation.ValidationService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Locale;

@Controller
@Profile("resthub-validation")
@RequestMapping("/api/validation")
public class ValidationController {

    @Inject
    @Named("validationService")
    private ValidationService validationService;

    @RequestMapping(value = "{canonicalClassName:.+}", method = RequestMethod.GET)
    @ResponseBody
    public ModelConstraint getConstraintsForClassName(@PathVariable String canonicalClassName, @RequestParam(required = false) String locale) {

        try {
            if (null == locale) {
                return this.validationService.getConstraintsForClassName(canonicalClassName);
            } else {
                return this.validationService.getConstraintsForClassName(canonicalClassName, new Locale(locale));
            }
        } catch (ClassNotFoundException e) {
            throw new NotFoundException("Class " + canonicalClassName + " could not be found", e);
        }
    }

}
