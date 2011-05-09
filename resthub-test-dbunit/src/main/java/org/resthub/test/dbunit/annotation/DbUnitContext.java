package org.resthub.test.dbunit.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation allows to specify the name of the {@link DbUnitConfiguration}
 * bean to use.
 * <p>
 * If only one {@link DbUnitConfiguration} bean is defined in application
 * context, it will be picked by default, so no need to specify this annotation
 * in this case.
 * </p>
 *<p>
 * This annotation can be inherited, so consider to put it on an abstract base
 * class for some of your tests.
 * </p>
 * @author vanackej
 */
@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DbUnitContext {

	/**
	 * The name of the configuration bean for DbUnit.
	 */
	String value();

}