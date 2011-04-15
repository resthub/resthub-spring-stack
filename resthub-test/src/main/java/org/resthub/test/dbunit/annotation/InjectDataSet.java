package org.resthub.test.dbunit.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * this annotation can be used on a JUnit test class or method in order to
 * inject a DbUnit dataset.
 * 
 * @author vanackej
 */
@Target( { ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface InjectDataSet {

	/**
	 * <p>
	 * Name(s) of the DbUnit dataset(s) to inject.
	 * </p>
	 * 
	 * <p>
	 * Names are resolved to actual datasets using configured {@link DataSetResolver}
	 * </p>
	 */
	String[] value();

	/**
	 * The database operation to perform. Default is
	 * {@link DBOperation#CLEAN_INSERT}
	 */
	DBOperation dbOperation() default DBOperation.CLEAN_INSERT;
	
	/**
	 * If used on test class, defines if injection is performed per test method or only once for test class.
	 * default is <code>true</code> (once for the class)
	 * <p>This parameter has no effect when the annotation is located on the test method</p>
	 */
	boolean onceForClass() default true;
}
