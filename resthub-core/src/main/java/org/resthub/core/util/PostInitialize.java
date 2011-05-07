package org.resthub.core.util;

import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.METHOD;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

/**
 * Similar to @PostConstruct Spring annoatation, but exectuted later in applicatin lefecycle, in order to get
 * transaction management ready.
 *   
 * Found on http://forum.springsource.org/showthread.php?p=252616#post252616
 * 
 * @author AlphaCSP
 */
@Documented
@Retention(RUNTIME)
@Target(METHOD)
public @interface PostInitialize {
    int order() default 0;
}
