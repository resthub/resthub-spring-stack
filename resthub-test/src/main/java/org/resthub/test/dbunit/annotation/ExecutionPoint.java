package org.resthub.test.dbunit.annotation;

/**
 * Describes where the action must take place.
 * 
 * @author a131199
 *
 */
public enum ExecutionPoint {

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
