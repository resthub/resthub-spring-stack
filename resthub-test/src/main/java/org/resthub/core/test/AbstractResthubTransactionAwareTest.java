/**
 * 
 */
package org.resthub.core.test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.junit.After;
import org.junit.Before;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Base class for your transaction aware unit tests
 * Your test won't be transactional itself, but will manage transaction if methods called in its tests are annotated Transactional
 * It is inspired from OpenSessionInViewFilter, and is suitable for Service classes unit tests
 * ContextConfiguration is preconfigured to scan your applicationContext.xml files from classpath  
 */
@TransactionConfiguration(defaultRollback = false)
@TestExecutionListeners(listeners = {
		DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		//DbUnitTestExecutionListener.class,
		TransactionalTestExecutionListener.class})
public abstract class AbstractResthubTransactionAwareTest extends AbstractResthubTest {
	
	@PersistenceUnit
	protected EntityManagerFactory emf;
	
	@Before
	public void setUpTransaction() {
		TransactionSynchronizationManager.bindResource(emf, new EntityManagerHolder(emf.createEntityManager()));
	}
	
	@After
	public void tearDownTransaction() {
		EntityManagerHolder emHolder = (EntityManagerHolder) TransactionSynchronizationManager.unbindResource(emf);
		emHolder.getEntityManager().close();
	}
		
}
