package org.resthub.tapestry5.components.mixins.service;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.services.LibraryMapping;
import org.apache.tapestry5.validator.ValidatorMacro;

public class ResthubComponentsModule {

    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration)
    {
        configuration.add(new LibraryMapping("resthub", "org.resthub.tapestry5.components"));
    }
    
    @Contribute(ValidatorMacro.class)
    public static void combineValidators(MappedConfiguration<String, String> configuration)
    {
        configuration.add("username", "required, minlength=3, maxlength=15");
        configuration.add("password", "required, minlength=6, maxlength=12");
    }
	
}
