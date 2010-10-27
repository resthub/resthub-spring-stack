package org.resthub.tapestry5.services;

import org.apache.tapestry5.ioc.annotations.SubModule;
import org.resthub.tapestry5.components.mixins.service.ResthubComponentsModule;
import org.resthub.tapestry5.jpa.services.ResthubJPAModule;
import org.resthub.tapestry5.validation.services.ResthubValidationModule;


@SubModule(
		{ ResthubValidationModule.class, ResthubJPAModule.class, ResthubComponentsModule.class})
public class ResthubModule {

}
