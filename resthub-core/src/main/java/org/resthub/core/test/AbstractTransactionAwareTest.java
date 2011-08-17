/**
 * 
 */
package org.resthub.core.test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.resthub.core.context.ResthubXmlContextLoader;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Base class for your transaction aware unit tests Your test won't be
 * transactional itself, but will manage transaction if methods called in its
 * tests are annotated Transactional It is inspired from
 * OpenSessionInViewFilter, and is suitable for Service classes unit tests
 * ContextConfiguration is preconfigured to scan your applicationContext.xml
 * files from classpath
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = ResthubXmlContextLoader.class, locations = { "classpath*:resthubContext.xml",
        "classpath*:applicationContext.xml" })
@TransactionConfiguration(defaultRollback = false)
public abstract class AbstractTransactionAwareTest {

    protected EntityManagerFactory emf;

    @PersistenceUnit
    public void setEntityManagerFactory(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Before
    public void setUp() {
        TransactionSynchronizationManager.bindResource(emf, new EntityManagerHolder(emf.createEntityManager()));
    }

    @After
    public void tearDown() {
        EntityManagerHolder emHolder = (EntityManagerHolder) TransactionSynchronizationManager.unbindResource(emf);
        emHolder.getEntityManager().close();
    }

}
