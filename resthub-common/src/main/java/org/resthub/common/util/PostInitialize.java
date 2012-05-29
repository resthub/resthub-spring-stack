package org.resthub.common.util;

import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.METHOD;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

/**
 * Similar to @PostConstruct Spring annotation, but executed later in application lifecycle, in order to get transaction
 * management ready. Useful for data initialization for example
 * 
 * Found on http://forum.springsource.org/showthread.php?p=252616#post252616
 * 
 * Usage : annotate a bean method (it should return void and have no argument) that you want to run once at application
 * startup
 * 
 * @author AlphaCSP
 */
@Documented
@Retention(RUNTIME)
@Target(METHOD)
public @interface PostInitialize {
    int order() default 0;
}
