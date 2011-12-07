/**
 * 
 */
package org.resthub.core.monitoring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Monitored annotation.
 * Used to monitor methods of a annoted class.
 * @author Nicolas Carlier <nicolas.carlier@atos.net>
 */
@Target({ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Monitored {

}
