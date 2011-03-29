package org.resthub.identity.front;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.eclipse.jetty.http.HttpHeaders;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.FilterMapping;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.resthub.identity.model.Group;
import org.resthub.oauth2.common.front.model.TokenResponse;
import org.resthub.web.jackson.JacksonProvider;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.representation.Form;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;

/**
 * Test to check management of ACL with Oauth2 filter.
 */
public class AclOauth2Test {

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
	Logger logger = Logger.getLogger(AclOauth2Test.class);

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

		// Jetty server conf.
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath(serverRoot);
        context.setBaseResource(org.eclipse.jetty.util.resource.Resource.newResource("src/main/webapp"));

        // Spring listener.
        context.addEventListener(new ContextLoaderListener());
        context.getInitParams().put("contextConfigLocation", "classpath*:resthubContext.xml " +
        		"classpath*:applicationContext.xml classpath:inMemoryApplicationContext.xml " +
        		"classpath:securityContext.xml");
        // JPA filter
        context.addFilter(OpenEntityManagerInViewFilter.class, "/*", 1);
        // OAuth2 filter
        FilterHolder oauth2Filter = new FilterHolder(DelegatingFilterProxy.class);
        oauth2Filter.setName("OAuth2Filter");
        context.addFilter(oauth2Filter, "/api/secured/*", FilterMapping.REQUEST);
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
	public void shouldAnonymousAccessBeRejected() {
		// Given a resource on the server
		WebResource server = resource();
		// When accessing a protected resource
		ClientResponse response = server.path("api/secured").delete(ClientResponse.class);
		// Then the result is an error.
		assertEquals(401, response.getClientResponseStatus().getStatusCode());
	} // shouldAnonymousAccessBeRejected().

	@Test
	public void shouldUnauthorizedAccessBeRejected() {
		// Given a resource on the server
		WebResource server = resource();
		// Given an authenticated user with insuffisant rights
		Form form = new Form();
		form.add("grant_type", "password");
		form.add("client_id", null);
		form.add("client_secret", null);
		form.add("username", "admin");
		form.add("password", "4dm|n");
		ClientResponse response = server.path("api/authorize/token").
			type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, form);
		TokenResponse tokenResponse = response.getEntity(TokenResponse.class);
		// Given a group
		Group group = new Group();
		group.setName("Hocus Pocus");
		
		// When accessing a protected resource
		response = server.path("api/secured").header(HttpHeaders.AUTHORIZATION, "OAuth "+tokenResponse.accessToken)
			.post(ClientResponse.class, group);
		// Then the result is an error.
		assertEquals(403, response.getClientResponseStatus().getStatusCode());
	} // shouldUnauthorizedAccessBeRejected().
	
	@Test
	public void shouldAccessProtectedWithRoleBeProcessed() {
		// Given a resource on the server
		WebResource server = resource();
		// Given an authenticated user with suffisant rights
		Form form = new Form();
		form.add("grant_type", "password");
		form.add("client_id", null);
		form.add("client_secret", null);
		form.add("username", "test");
		form.add("password", "t3st");
		ClientResponse response = server.path("api/authorize/token").
			type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, form);
		TokenResponse tokenResponse = response.getEntity(TokenResponse.class);
		// Given a group
		Group group = new Group();
		group.setName("Hocus Pocus");
		
		// When accessing an API with a role.
		response = server.path("api/secured").header(HttpHeaders.AUTHORIZATION, "OAuth "+tokenResponse.accessToken)
			.post(ClientResponse.class, group);
		// Then the result is an error.
		assertEquals(200, response.getClientResponseStatus().getStatusCode());
	} // shouldAccessProtectedWithRoleBeProcessed().
	
	@Test
	public void shouldAccessProtectedWithPermissionBeProcessed() {
		// Given a resource on the server
		WebResource server = resource();
		// Given an authenticated user with suffisant rights
		Form form = new Form();
		form.add("grant_type", "password");
		form.add("client_id", null);
		form.add("client_secret", null);
		form.add("username", "test");
		form.add("password", "t3st");
		ClientResponse response = server.path("api/authorize/token").
			type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, form);
		TokenResponse tokenResponse = response.getEntity(TokenResponse.class);
		// Given a group created
		Group group = new Group();
		group.setName("Incubus");
		group = server.path("api/secured").header(HttpHeaders.AUTHORIZATION, "OAuth "+tokenResponse.accessToken)
			.post(Group.class, group);
		// When accessing an API with a permission.
		response = server.path("api/secured/"+group.getId()).header(HttpHeaders.AUTHORIZATION, "OAuth "+tokenResponse.accessToken)
			.delete(ClientResponse.class);
		// Then the result is an error.
		assertEquals(204, response.getClientResponseStatus().getStatusCode());
	} // shouldAccessProtectedWithPermissionBeProcessed().
	
} // Class AclOauth2Test.
