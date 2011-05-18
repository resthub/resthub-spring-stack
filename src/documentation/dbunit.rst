.. highlight:: java

================================
DBUnit integration
================================

Introduction
============

When designing unit tests with a database, you need to manage the state of the database in order to have repeatable tests.

Through RESThub, we propose a simple way to deal with these concerns. but first just make some clarification.

Why an embedded database is not enough
--------------------------------------

The first step is to use an embedded memory database. Thus, the database is created each time you launch your test suite and you are sure that no existing data can corrupt your tests results. But as your tests are executed, they can alter the state of the database. Any test method changing the database state can break subsequents tests.

At this point, you may think : "Just enclose your test in a transaction and rollback it after the test". Proceeding this way, you are sure to preserve the database state. But you will also miss all errors that are discovered at commit or flush time. Indeed, JPA providers like Hibernate can cache the SQL instructions they are supposed to send to the database, often until you actually commit the transaction.

You definitively need a way to restore the database state before each test. That's exactly what DBUnit is made for.

DBUnit
------

// TODO : Quick presentation of dbunit

See http://dbunit.sourceforge.net/

Integrating DBUnit and Spring Test
==================================

// TODO : describe boilerplate code, configuration problems depending on the underlying database and how we avoid it

Configuration
-------------

Using plain Spring beans
~~~~~~~~~~~~~~~~~~~~~~~~

.. code-block:: xml

	<?xml version="1.0" encoding="UTF-8"?>
	<beans xmlns="http://www.springframework.org/schema/beans" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xmlns:dbunit="http://www.resthub.org/schema/dbunit"
		xsi:schemaLocation="
			http://www.springframework.org/schema/beans 
			http://www.springframework.org/schema/beans/spring-beans-3.0.xsd">
	
		<bean id="dbunitConfiguration" class="org.resthub.test.dbunit.config.DbUnitConfiguration">
			<property name="databaseConnection" ref="databaseConnection"/>
			<property name="databaseTester" ref="databaseTester"/>
		</bean>
		
		<bean id="databaseConnection" class="org.resthub.test.dbunit.config.DatabaseConnectionFactory">
			<property name="dataSource" ref="dataSource"/>
		</bean>
		
		<bean id="databaseTester" class="org.dbunit.DefaultDatabaseTester">
			<constructor-arg ref="databaseConnection"/>
		</bean>
	
	</beans>

*Note* : *databaseTester* bean is not mandatory, it is just a helper that you can use in your test to check the database state.
	
Using the dbunit namespace
~~~~~~~~~~~~~~~~~~~~~~~~~~

The *dbunit* namespace avoid complicated DBUnit configuration. The configuration below is equivalent to the one just above:

.. code-block:: xml

	<?xml version="1.0" encoding="UTF-8"?>
	<beans xmlns="http://www.springframework.org/schema/beans" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xmlns:dbunit="http://www.resthub.org/schema/dbunit"
		xsi:schemaLocation="
			http://www.springframework.org/schema/beans 
			http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
			http://www.resthub.org/schema/dbunit 
			http://www.resthub.org/schema/dbunit/resthub-dbunit-1.0.xsd">

		<dbunit:configuration data-source="dataSource"/>
	
	</beans>

That's it : DBUnit is configured and ready to use in your unit tests.

Including/Excluding tables
~~~~~~~~~~~~~~~~~~~~~~~~~~

Sometimes, you may need to exclude some tables. // TODO give some use cases
Tables inclusion/exclusion is available through the namespace configuration :

.. code-block:: xml

	<dbunit:configuration data-source="dataSource">
		<dbunit:include-table>user_*</dbunit:include-table>
		<dbunit:include-table>roles</dbunit:include-table>
		<dbunit:exclude-table>quartz_*</dbunit:exclude-table>
	<dbunit:configuration/>

You can use the full table name or wildcards : * for any number of characters or ? for just one character.

Writing the test case
---------------------

Here is a simple test case::

	@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
	@TransactionConfiguration(defaultRollback = false)
	@InjectDataSet("dataset")
	@RunWith(SpringJUnit4ClassRunner.class)
	@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		DbUnitTestExecutionListener.class,
		TransactionalTestExecutionListener.class })
	public class DBUnitTestCase {

		@Test
		public void testXXX() throws Exception {
			// TODO test something
		}
	
		@Test
		public void testSomethingThatChangeTheDatabaseState() throws Exception {
			// TODO test something that change the database state
		}
	
		@Test
		@InjectDataSet("dataset")
		public void testYYY() throws Exception {
			// Another test
		}
	}

The main points to remember are :

* The test case must use the *DbUnitTestExecutionListener* in order to activate DBUnit support.
* The test class is annotated with @InjectDataSet. Thanks to this annotation, the dataSet named 'dataset' is injected one time before the test class.
* Because the second test method changes the database state, we choose to inject the dataSet again on the last method.

You can also combine serveral datasets together::
	
	@InjectDataSet({"dataset1", "dataset2"})

Writing datasets
----------------

There are 3 options to create a dataset :

* Using a DBUnit flat XML file
* Using SQL scripts
* Using a custom Spring bean

Lets explore these options.

DBUnit flat XML file
~~~~~~~~~~~~~~~~~~~~

DBUnit has its own XML dataSet file format. You can find more information about this format here: http://dbunit.sourceforge.net/apidocs/org/dbunit/dataset/xml/FlatXmlDataSet.html

You can declare an XML dataSet using plain Spring bean declaration :

.. code-block:: xml

	<bean id="dataset" class="org.resthub.test.dbunit.initializer.FlatXMLDatabaseInitializer">
		<property name="location" value="classpath:datasets/dataset.xml"/>
	</bean>

The *location* attribute use Spring resource syntax.
	
If you use the namespace, the following configuration is equivalent:

.. code-block:: xml

	<dbunit:flatxml-dataset id="dataset" location="classpath:datasets/dataset.xml"/>

SQL scripts
~~~~~~~~~~~

// TODO sample

Custom Spring bean
~~~~~~~~~~~~~~~~~~

The custom Spring bean is the most powerfull solution. You can initialize the database with any Java code, including using JPA , and let the framework create the dataSet by taking a snapshot of the database after your code has been executed.

Here is a simple DatabaseInitializer implementation::

	@Named("sampleDataset")
	public class SampleDatabaseInitializer implements DatabaseInitializer {

		public static final String ENTITY_REF = "ref";
		public static final String ENTITY_DESCRIPTION = "entity description";

		@PersistenceContext
		private EntityManager entityManager;

		@Override
		@Transactional
		public void initDatabase() throws Exception {
			SampleEntity entity = new SampleEntity();
			entity.setRef(ENTITY_REF);
			entity.setDescription(ENTITY_DESCRIPTION);
			entityManager.persist(entity);
		}
	}

Again, the bean name must match the expected dataset name. In this case, the dataset can be referenced by the name "sampleDataset".

Best practices
--------------

* Consider declaring DBUnit and Spring Test annotations on an abstract parent class, and make all your tests inherit from this parent.
* Use constants when initializing you dataset with Java code, and reference these constants in your unit tests. Doing so, you will improve the maintenability of your tests 