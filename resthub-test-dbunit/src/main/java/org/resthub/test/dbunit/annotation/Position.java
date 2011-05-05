package org.resthub.test.dbunit.annotation;

/**
 * Describes where the action must take place.
 * 
 * @author vanackej
 *
 */
public enum Position {

	/**
	 * Executes before test class or method
	 */
	BEFORE,
	
	/**
	 * Executes after test class or method
	 */
	AFTER,
	
	/**
	 * Executes before and after test class or method
	 */
	BOTH;
	
}
