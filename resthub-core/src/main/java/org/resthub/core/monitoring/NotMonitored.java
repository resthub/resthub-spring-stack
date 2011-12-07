/**
 * 
 */
package org.resthub.core.monitoring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * NotMonitored annotation.
 * Used to not monitor a specific method.
 * @author Nicolas Carlier <nicolas.carlier@atos.net>
 */
@Target({ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotMonitored {

}
