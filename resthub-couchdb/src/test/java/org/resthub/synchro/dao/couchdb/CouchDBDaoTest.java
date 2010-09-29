package org.resthub.synchro.dao.couchdb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test of couchdb implementation of SynchroDAO.
 */
public class CouchDBDaoTest {

	// -----------------------------------------------------------------------------------------------------------------
	// Attributes

	/**
	 * The tested User DAO
	 */
	protected UserDao userDao = new UserDao();

	/**
	 * The tested Group DAO
	 */
	protected GroupDao groupDao = new GroupDao();

	/**
	 * Id of the tested POJO
	 */
	protected String id;

	// -----------------------------------------------------------------------------------------------------------------
	// Setup/finalize

	@Before
	public void setUp() throws Exception {
		String host = "localhost";
		int port = 5984;
		String database = "test";
		
		// Destroy existing database.
		HttpClient httpClient = new StdHttpClient.Builder()
	        .host(host)
	        .port(port)
	        .build();
		CouchDbInstance db = new StdCouchDbInstance(httpClient);
		List<String> existingDB = db.getAllDatabases();
		if (existingDB.contains(database)) {
			db.deleteDatabase(database);
		}
		
		// Initialized DAO.
		userDao.setHost(host);
		userDao.setPort(port);
		userDao.setDatabase(database);
		groupDao.setHost(host);
		groupDao.setPort(port);
		groupDao.setDatabase(database);
		
		// Creates a new DAO.
		User resource = new User();
		resource.firstName = "toto";
		resource.lastName = "tata";
		resource = userDao.save(resource);
		this.id = resource.getId();
	} // setUp().

	// -----------------------------------------------------------------------------------------------------------------
	// Tests methods

	@Test
	public void testSave() throws Exception {
		String userFirstName = "Louis";
		String userLastName = "De Funes";

		User resource = new User();
		resource.firstName = userFirstName;
		resource.lastName = userLastName;
		resource = userDao.save(resource);
			
		User foundResource = userDao.get(resource.getId());
		assertNotNull("Resource not found!", foundResource);
		assertEquals("User first name not serialized", userFirstName, resource.firstName);
		assertEquals("User last name not serialized", userLastName, resource.lastName);
		
		resource.firstName = userFirstName+"*";
		userDao.save(resource);
		
		foundResource = userDao.get(resource.getId());
		assertNotNull("Resource not found!", foundResource);
		assertEquals("User first name not serialized", userFirstName+"*", resource.firstName);
		assertEquals("User last name not serialized", userLastName, resource.lastName);
	}

	@Test
	public void testDelete() throws Exception {
		userDao.remove(this.id);

		User resource = userDao.get(this.id);
		assertNull("Resource not deleted!", resource);
	}

	@Test
	public void testFindAll() throws Exception {
		List<User> resourceList = userDao.getAll();
		assertTrue("No resources found!", resourceList.size() == 1);
	}
	
	@Test
	public void testRelationship() throws Exception {
		Group g1 = new Group();
		g1.name = "group 1";
		groupDao.save(g1);
		assertNotNull("Group g1 not found!", g1.getId());
		
		User u1 = new User();
		u1.firstName = "Jean";
		u1.lastName = "Dujardin";
		userDao.save(u1);
		assertNotNull("User u1 not found!", u1.getId());
		
		g1.members.add(u1);
		groupDao.save(g1);
		Group foundGroup = groupDao.get(g1.getId());
		assertTrue("User u1 not a member of g1", foundGroup.members.contains(u1));
		
		/*u1.firstName = "Gérard";
		userDao.save(u1);
		User foundUser = userDao.get(u1.getId());
		assertEquals("User u1 first name has not serialized", "Gérard", foundUser.firstName);
		assertEquals("User u1 in g1 first name has not serialized", "Gérard", foundGroup.members.get(0).firstName);
		*/
		g1.members.remove(0);
		groupDao.save(g1);
		foundGroup = groupDao.get(g1.getId());
		assertFalse("User u1 is still a member of g1", foundGroup.members.contains(u1));
	}


} // Class CouchDBDaoTest
