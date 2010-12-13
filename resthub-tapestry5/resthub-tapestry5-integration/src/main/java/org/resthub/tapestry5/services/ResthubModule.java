package org.resthub.tapestry5.services;

import org.apache.tapestry5.ioc.annotations.SubModule;
import org.resthub.tapestry5.components.mixins.service.ResthubComponentsModule;
import org.resthub.tapestry5.jpa.services.ResthubJPAModule;
import org.resthub.tapestry5.validation.services.ResthubValidationModule;


/**
 * Provide configuration for Tapestry5 resthub integration module : contributions
 * of services, behaviours, configuration parameters, libraries, etc.
 * 
 * Basically, only provide inclusion of all others nested resthub Tapestry5 sub modules
 * 
 * @author bmeurant <Baptiste Meurant>
 *
 */
@SubModule(
		{ ResthubValidationModule.class, ResthubJPAModule.class, ResthubComponentsModule.class})
public class ResthubModule {

}
