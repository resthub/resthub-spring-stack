/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.resthub.test.dbunit.annotation;

import org.junit.runners.model.InitializationError;
import org.resthub.test.dbunit.DbUnitTestContextManager;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test runner that preconfigure test execution listeners for DBunit + Spring
 * support
 */
public class DbUnitSpringJUnit4ClassRunner extends SpringJUnit4ClassRunner {

    public DbUnitSpringJUnit4ClassRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
    }

    @Override
    protected TestContextManager createTestContextManager(Class<?> clazz) {
        return new DbUnitTestContextManager(clazz, getDefaultContextLoaderClassName(clazz));
    }

}
