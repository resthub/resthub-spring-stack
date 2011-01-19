package org.resthub.oauth2.filter.front;

import static org.junit.Assert.assertTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.jasper.servlet.JspServlet;
import org.eclipse.jetty.http.HttpHeaders;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.FilterMapping;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.junit.AfterClass;
import org.resthub.oauth2.filter.service.MockTokenProcessingService;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.context.ContextLoaderListener;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;

/**
 * Abstract class for OAuth2 filter tests :
 * Launches two Jetty in-memory servers:
 * - one for a mock resource server, protected with tested filters
 * - one for a mock authorization server, used by tested filters.
 */
public class AbstractOAuth2FilterTest {


	// -----------------------------------------------------------------------------------------------------------------
	// Static private attributes

	/**
	 * Jetty memory server instance for resource server.
	 */
	protected static Server resourceServer;
	
	/**
	 * Jetty memory server instance for authorization server.
	 */
	protected static Server authorizationServer;
	
	/**
	 * Jetty memory server port.
	 */
	protected static int resourceServerPort = 9796;
	
	/**
	 * Jetty memory server port.
	 */
	protected static int authorizationServerPort = 9797;
 
	/**
	 * Spring configuration locations
	 */
	protected static String contextLocations = "classpath*:resthubContext.xml classpath:applicationContext.xml";

	/**
	 * Url of the Redirection Filter
	 */
	public static final String redirectionFilterUrl = "/notAllowed";

	/**
	 * Root of the Authorization server.
	 */
	public static final String authorizationServerRoot = "/authorization";

	/**
	 * Name of the parameter containing the requested url when incoming request are rejected.
	 */
	public static final String targetUrlParameter = "requested";

	/**
	 * Client id used by the redirection filter to redirect incoming requests. 
	 */
	public static final String resourceClientId = "";

	/**
	 * Client secret used by the redirection filter to obtain tokens from codes. 
	 */
	public static final String resourceClientSecret = "";
	
	// -----------------------------------------------------------------------------------------------------------------
	// Test suite initialization and finalization

	/**
	 * Start an in-memory mock authorization server.
	 * To be used in test initialization.
	 */
	public static void startAuthorizationServer() throws Exception {
		// Creates a Jetty for the authorization server.
		authorizationServer = new Server(authorizationServerPort);

		// On lance le serveur Jetty
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath(authorizationServerRoot);

        context.getInitParams().put("contextConfigLocation", "classpath*:resthubContext.xml classpath:authorizationServerContext.xml");
        context.setBaseResource(Resource.newResource("src/main/webapp"));
        context.addFilter(OpenEntityManagerInViewFilter.class, "/*", 1);
        context.addServlet(JspServlet.class, "/jsp/*");
        context.addServlet(SpringServlet.class, "/api/*");
        context.addEventListener(new ContextLoaderListener());

		// Jetty start.
		authorizationServer.setHandler(context);
		authorizationServer.start();		
	} // startAuthorizationServer().
	
	/**
	 * Start an in-memory mock resource server.
	 * To be used in test initialization.
	 */
	public static void startResourceServer() throws Exception {
		// Creates a Jetty for the resource server.
		resourceServer = new Server(resourceServerPort);

		// Configures it
		ServletContextHandler context = new ServletContextHandler(
				ServletContextHandler.SESSIONS);
		context.setContextPath("/inmemory");
		
		// Tested filter: redirection
        FilterHolder redirectionFilter = new FilterHolder(OAuth2RedirectionFilter.class);
        redirectionFilter.setName("oauth2RedirectionFilter");
        redirectionFilter.setInitParameter("authorizationServer", "http://localhost:"+authorizationServerPort+
        		authorizationServerRoot+"/api/authorize");
        redirectionFilter.setInitParameter("targetUrlParameter", targetUrlParameter);
        redirectionFilter.setInitParameter("clientId", resourceClientId);
        redirectionFilter.setInitParameter("clientSecret", resourceClientSecret);
        redirectionFilter.setInitParameter("tokenProcessingServiceClass", MockTokenProcessingService.class.getName());
		context.addFilter(redirectionFilter, redirectionFilterUrl, FilterMapping.REQUEST);
		
		// Tested filter: protection
        FilterHolder protectionFilter = new FilterHolder(OAuth2Filter.class);
        protectionFilter.setName("oauth2Filter");
        protectionFilter.setInitParameter("securedResourceName", "myResource");
        protectionFilter.setInitParameter("validationServiceClass", 
        		"org.resthub.oauth2.filter.service.MockExternalValidationService");
		context.addFilter(protectionFilter, "/*", FilterMapping.REQUEST);
		
		// Jersey Servlet
		ServletHolder servlet = new ServletHolder(ServletContainer.class);
		servlet.setInitParameter("com.sun.jersey.spi.container.ResourceFilters", 
				"com.sun.jersey.api.container.filter.RolesAllowedResourceFilterFactory");
		servlet.setInitParameter("com.sun.jersey.config.property.packages", 
				"org.resthub.oauth2.filter.mock.resource;org.resthub.oauth2.filter.front");
		context.addServlet(servlet, "/*");
		
		// Jetty start.
		resourceServer.setHandler(context);
		resourceServer.start();
	} // suiteSetUp().

	/**
	 * After the test suite, stops the Jetty inmemory server.
	 */
	@AfterClass
	public static void suiteTearDown() throws Exception {
		if (resourceServer != null) {
			resourceServer.stop();
		}
		if (authorizationServer != null) {
			authorizationServer.stop();
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
		Client client = Client.create(config);
		return client.resource("http://localhost:" + resourceServerPort +"/inmemory");
	} // resource()

	/**
	 * Checks the syntax of authenticate header, and extract the error case.
	 * 
	 * @param header The 
	 * @return The error case. Can be null.
	 */
	protected String checkAuthenticateHeader(ClientResponse response) {
		assertTrue("Authenticate header is not present", response.getHeaders().containsKey(
				HttpHeaders.WWW_AUTHENTICATE));
		String error = response.getHeaders().get(HttpHeaders.WWW_AUTHENTICATE).get(0);
		assertTrue("Authenticate header value is not valid", error.matches("Token realm=\".*\"(, error=\".*\"(, " +
				"error_description=\".*\"(, error_uri=\".*\")?)?)?"));
		Pattern p = Pattern.compile("error=\"([^\"]*)\"");
		Matcher m = p.matcher(error);
		m.find();
		String cause = null;
		try {
			cause = m.group(1);
		} catch (IllegalStateException exc) {
			// No match found.
		}
		return cause;
	} // checkAuthenticateHeader().

} // Class AbstractOAuth2FilterTest
