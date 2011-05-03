package org.resthub.identity.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.resthub.core.model.Resource;
import org.resthub.identity.model.User;
import org.resthub.web.jackson.JacksonProvider;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.context.ContextLoaderListener;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;

/**
 */
public class SearchControllerTest {

	// -----------------------------------------------------------------------------------------------------------------
	// Static private attributes
	
	/**
	 * Jetty memory server port.
	 */
	protected static int serverPort = 9797;

	/**
	 * Root of the in-memory server.
	 */
	public static final String serverRoot = "/identity";

	/**
	 * Jetty memory server instance.
	 */
	protected static Server server;

	/**
	 * Test class logger.
	 */
	Logger logger = Logger.getLogger(SearchControllerTest.class);

	// -----------------------------------------------------------------------------------------------------------------
	// Test suite initialization and finalization

	/**
	 * Start an in-memory mock authorization server.
	 * To be used in test initialization.
	 */
	@BeforeClass
	public static void suiteSetUp() throws Exception {
		// Creates a Jetty for the authorization server.
		server = new Server(serverPort);

		// On lance le serveur Jetty
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath(serverRoot);
        context.setBaseResource(org.eclipse.jetty.util.resource.Resource.newResource("src/main/webapp"));

        // Spring listener.
        context.addEventListener(new ContextLoaderListener());
        context.getInitParams().put("contextConfigLocation", "classpath*:resthubContext.xml " +
        		"classpath*:applicationContext.xml");
        // JPA filter
        context.addFilter(OpenEntityManagerInViewFilter.class, "/*", 1);
        // Jersey Servlet.
        ServletHolder jerseyServlet = new ServletHolder(SpringServlet.class);
        context.addServlet(jerseyServlet, "/api/*");
            
		// Jetty start.
		server.setHandler(context);
		server.start();		
	} // suiteSetUp().
	

	/**
	 * After the test suite, stops the Jetty inmemory server.
	 */
	@AfterClass
	public static void suiteTearDown() throws Exception {
		if (server != null) {
			server.stop();
		}
	} // suiteTearDown().

	// -----------------------------------------------------------------------------------------------------------------
	// Protected methods

	/**
	 * Returns an object to interact with the inmemory server.
	 * 
	 * @return A Jersey HTTP client.
	 */
	protected WebResource resource() {
        ClientConfig config = new DefaultClientConfig();
        config.getSingletons().add(new JacksonProvider());
     
        Client client = Client.create(config);
		return client.resource("http://localhost:" + serverPort + serverRoot);
	} // resource()
	
	// -----------------------------------------------------------------------------------------------------------------
	// Tests

	@Test
	public void shouldIndexesBeReseted() {
		// Given a resource on the server
		WebResource server = resource();
		// When reseting indexes
		ClientResponse response = server.path("/api/search").put(ClientResponse.class);
		// Then the operation is processed
		assertEquals(204, response.getClientResponseStatus().getStatusCode());
	} // shouldIndexesBeReseted().

	@Test
	public void shouldNullQueryFailed() {
		// Given a resource on the server
		WebResource server = resource();
		// When searching without parameter
		ClientResponse response = server.path("/api/search").get(ClientResponse.class);
		// Then the result is an error.
		assertEquals(500, response.getClientResponseStatus().getStatusCode());
		String responseStr = response.getEntity(String.class);
		assertTrue(responseStr.contains("query must not be null"));
	} // shouldEmptyEmptyQueryReturnNothing().
	
	@Test
	public void shouldEmptyQueryFailed() {
		// Given a resource on the server
		WebResource server = resource();
		// When searching with empty query
		ClientResponse response = server.path("/api/search").queryParam("query", "")
				.get(ClientResponse.class);
		// Then the result is empty.
		assertEquals(500, response.getClientResponseStatus().getStatusCode());
		String responseStr = response.getEntity(String.class);
		assertTrue(responseStr.contains("Misformatted queryString"));
	} // shouldEmptyQueryFailed().
	
	@Test
	public void shouldUnmatchingQueryReturnsEmptyResults() {
		// Given a resource on the server
		WebResource server = resource();
		// When searching with an unmatching query
		Resource[] results = server.path("/api/search").queryParam("query", "toto")
				.get(Resource[].class);
		// Then the result is empty.
		assertNotNull(results);
		assertEquals(0, results.length);
	} // shouldUnmatchingQueryReturnsEmptyResults().
		
	@Test
	public void shouldQueryReturnsUsers() {
		// Given a resource on the server
		WebResource server = resource();
		// Given a user with jdujardin as login
		User user = new User();
		user.setLogin("jdujardin");
		user.setPassword("pwd");
		user = server.path("/api/user/").post(User.class, user);
		
		// Given a user with jean as name
		User user2 = new User();
		user2.setLogin("user2");
		user2.setLastName("jean");
		user2.setPassword("pwd");
		user2 = server.path("/api/user/").post(User.class, user2);

		// When searching the created user
		User[] results = server.path("/api/search").queryParam("query", "j")
				.get(User[].class);
		// Then the result contains the user.
		assertNotNull(results);
		assertEquals(2, results.length);
		assertEquals(user, results[0]);
		assertEquals(user2, results[1]);
	} // shouldQueryReturnsUsers().
	
	
	@Test
	public void shouldQueryReturnsUsersInJson() {
		// Given a resource on the server
		WebResource server = resource();
		// Given a user with jdujardin as login
		User user = new User();
		user.setLogin("jdujardin");
		user.setPassword("pwd");
		user = server.path("/api/user/").post(User.class, user);
		
		// Given a user with jean as name
		User user2 = new User();
		user2.setLogin("user2");
		user2.setLastName("jean");
		user2.setPassword("pwd");
		user2 = server.path("/api/user/").post(User.class, user2);

		ClientResponse response = server.path("/api/search").queryParam("query", "j")
				.accept(MediaType.APPLICATION_JSON)
				.get(ClientResponse.class);
		System.out.println(response.getEntity(String.class));
		
		// When searching the created user in JSON
		User[] results = server.path("/api/search").queryParam("query", "j")
				.accept(MediaType.APPLICATION_JSON)
				.get(User[].class);
		// Then the result contains the user.
		assertNotNull(results);
		assertEquals(2, results.length);
		assertEquals(user, results[0]);
		assertEquals(user2, results[1]);
	} // shouldQueryReturnsUsersInJson().
	
	@Test
	public void shouldQueryWithoutUsersNotReturnsUsers() {
		// Given a resource on the server
		WebResource server = resource();
		// Given a user
		User user = new User();
		user.setLogin("jdujardin");
		user.setPassword("pwd");
		user = server.path("/api/user/").post(User.class, user);

		// When searching the created user without users
		User[] results = server.path("/api/search").queryParam("query", "j")
				.queryParam("users", "false")
				.get(User[].class);
		// Then the result does not contains the user.
		assertNotNull(results);
		assertEquals(0, results.length);
	} // shouldQueryReturnsUsers().
	
} // Class SearchControllerTest.
