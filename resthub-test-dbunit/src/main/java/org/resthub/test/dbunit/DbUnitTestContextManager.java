/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.resthub.test.dbunit;

import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.TestExecutionListener;

/**
 * 
 * @author A206740
 */
public class DbUnitTestContextManager extends TestContextManager {

    private static final Log logger = LogFactory.getLog(DbUnitTestContextManager.class);

    private static final String[] DEFAULT_DBUNIT_TEST_EXECUTION_LISTENER_CLASS_NAMES = new String[] {
            "org.springframework.test.context.support.DependencyInjectionTestExecutionListener",
            "org.springframework.test.context.support.DirtiesContextTestExecutionListener",
            "org.resthub.test.dbunit.DbUnitTestExecutionListener",
            "org.springframework.test.context.transaction.TransactionalTestExecutionListener" };

    public DbUnitTestContextManager(Class<?> testClass) {
        super(testClass);
    }

    public DbUnitTestContextManager(Class<?> testClass, String defaultContextLoaderClassName) {
        super(testClass, defaultContextLoaderClassName);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Set<Class<? extends TestExecutionListener>> getDefaultTestExecutionListenerClasses() {
        Set<Class<? extends TestExecutionListener>> defaultListenerClasses = new LinkedHashSet<Class<? extends TestExecutionListener>>();
        for (String className : DEFAULT_DBUNIT_TEST_EXECUTION_LISTENER_CLASS_NAMES) {
            try {
                defaultListenerClasses.add((Class<? extends TestExecutionListener>) getClass().getClassLoader()
                        .loadClass(className));
            } catch (Throwable ex) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Could not load default TestExecutionListener class [" + className
                            + "]. Specify custom listener classes or make the default listener classes available.");
                }
            }
        }
        return defaultListenerClasses;
    }
}
