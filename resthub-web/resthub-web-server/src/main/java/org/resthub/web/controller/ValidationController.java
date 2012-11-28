package org.resthub.web.controller;

import org.resthub.common.exception.NotFoundException;
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
    public ModelConstraint getConstraintsForClassName(@PathVariable String canonicalClassName, @RequestParam(required = false) String locale, @RequestParam(required = false, defaultValue = "false") Boolean keyOnly) {

        Locale loc = null;

        try {
            if (keyOnly) {
                return this.validationService.getConstraintsForClassName(canonicalClassName, keyOnly);
            } else {
                if (locale != null) {
                    String[] locs = locale.split("-");

                    if (locs.length > 2) {
                        loc = new Locale(locs[0], locs[1], locs[2]);
                    } else if (locs.length > 1) {
                        loc = new Locale(locs[0], locs[1]);
                    } else {
                        loc = new Locale(locs[0]);
                    }
                }

                return this.validationService.getConstraintsForClassName(canonicalClassName, loc);
            }
        } catch (ClassNotFoundException e) {
            throw new NotFoundException("Class " + canonicalClassName + " could not be found", e);
        }
    }

}
