package org.resthub.web.controller;

import org.resthub.common.exception.NotFoundException;
import org.resthub.web.validation.ModelConstraint;
import org.resthub.web.validation.ValidationService;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Locale;

/**
 * Exposes dedicated API to export defined validation constraints related to object class name.
 *
 * This API is defined under the "resthub-validation" Spring profile and can be activated by adding
 * this profile in WebappIniliatizer. e.g:
 *
 * <br/><br/>
 * <pre>
 * {@code appContext.getEnvironment().setActiveProfiles("resthub-validation");}
 * </pre>
 */
@RestController
@Profile("resthub-validation")
@RequestMapping("/api/validation")
public class ValidationController {

    private ValidationService validationService;

    @Inject
    @Named("validationService")
    public void setService(ValidationService validationService) {
        this.validationService = validationService;
    }

    /**
     * Exposes an "/api/validation/{canonicalClassName}" endpoint to get all validation constraints for a given
     * object className and optional locale parameter.
     *
     * Usage:
     * <pre>
     * http://{host}:{port}/api/validation/org.resthub.validation.model.User
     * http://{host}:{port}/api/validation/org.resthub.validation.model.User?locale=en-us
     * </pre>
     *
     * @see ValidationService#getConstraintsForClassName
     * @see ModelConstraint
     *
     * @param canonicalClassName mandatory path parameter containing the complete className of the Java Bean to export
     *                           (i.e. package + className - e.g. {@code org.resthub.validation.model.User}).
     * @param locale optional request parameter, the API takes the locale string indicating your internationalization preferences.
     *               You can then provide a valid i18n locale string to choose the desired message locale.
     *               Available locales are those supported by Hibernate Validator or provided by your custom properties files. If no locale
     *               parameter is provided or if the locale parameter is invalid, the default server locale is used.
     * @return a {@code ModelConstraint} instance containing representation of all validation constraints for
     * the given className.
     *
     * @throws org.resthub.common.exception.NotFoundException if either canonicalClassName is missing or could not be retrieved.
     */
    @RequestMapping(value = "{canonicalClassName:.+}", method = RequestMethod.GET)
    public ModelConstraint getConstraintsForClassName(@PathVariable String canonicalClassName, @RequestParam(required = false) String locale) {

        Locale loc = null;

        try {
            if (locale != null) {
                loc = parseLocale(locale);
            }
            return this.validationService.getConstraintsForClassName(canonicalClassName, loc);
        } catch (ClassNotFoundException e) {
            throw new NotFoundException("Class " + canonicalClassName + " could not be found", e);
        }
    }

    /**
     * Parse a String represented locale (e.g. "en-us" or "fr-fr") to build a new {@code Locale} instance
     *
     * @param locale String representation of the desired locale
     *
     * @return a new {@link Locale} instance built from locale String parameter
     */
    Locale parseLocale(String locale) {
        Locale loc;
        String[] locs = locale.split("-");

        if (locs.length > 2) {
            loc = new Locale(locs[0], locs[1], locs[2]);
        } else if (locs.length > 1) {
            loc = new Locale(locs[0], locs[1]);
        } else {
            loc = new Locale(locs[0]);
        }
        return loc;
    }

}
