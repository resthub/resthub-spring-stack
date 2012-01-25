package org.resthub.oauth2.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.resthub.oauth2.httpclient.OAuth2Credentials;
import org.resthub.web.client.ClientFactory;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;

/**
 * Tests the the TokenFactory utility class. Launches an in-memory jetty server
 * with two contexts:
 * <ol>
 * <li>an authorization service build upon resthub-oauth2-provider.</li>
 * <li>a resource service protected with resthub-oauth2-filter.</li>
 * </ol>
 */
public class TestOAuth2Client {

    // -----------------------------------------------------------------------------------------------------------------
    // Static private attributes

    public static final String CLIENT_ID = "test";
    public static final String CLIENT_SECRET = "";
    public static final int PORT = 9796;
    public static final String BASE_URL = "http://localhost:" + PORT;
    public static final String AUTHENTICATION_ENDPOINT = BASE_URL + "/oauth/token";
    public static final String contextClass = "org.resthub.web.context.ResthubXmlWebApplicationContext";

    /**
     * Jetty memory server instance.
     */
    protected static Server server;

    /**
     * Before the test suite, launches a Jetty in memory server.
     */
    @BeforeClass
    public static void suiteSetUp() throws Exception {
        // Creates a Jetty server.
        server = new Server(PORT);

        // Add a context for authorization service
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.getInitParams().put("contextClass", contextClass);
        context.setDisplayName("resthub test webapp");
                
        FilterHolder filterDef = new FilterHolder(DelegatingFilterProxy.class);
        filterDef.setName("springSecurityFilterChain");
        filterDef.setInitParameter("contextAttribute", "org.springframework.web.servlet.FrameworkServlet.CONTEXT.spring");
        context.addFilter(filterDef, "/*", 0);
                
        ServletHolder servletHolder = new ServletHolder(DispatcherServlet.class);
        servletHolder.setName("spring");
        servletHolder.setInitOrder(1);
        servletHolder.setInitParameter("contextConfigLocation", "classpath*:resthubContext.xml classpath*:applicationContext.xml");
        context.addServlet(servletHolder, "/");

        // Starts the server.
        server.setHandler(context);

        server.start();
    }

    /**
     * After the test suite, stops the Jetty inmemory server.
     */
    @AfterClass
    public static void suiteTearDown() throws Exception {
        if (server != null) {
            server.stop();
        }
    }

    @Test
    public void testOAuth2SuccessfulRequest() throws ClientProtocolException, IOException {
        Client client = OAuth2ClientFactory.create(new OAuth2Credentials(AUTHENTICATION_ENDPOINT, CLIENT_ID,
                CLIENT_SECRET, "test", "t3st"));
        String result = client.resource(BASE_URL + "/api/resource/hello").get(String.class);
        assertEquals("Hello", result);
    }

    @Test(expected = UniformInterfaceException.class)
    public void testUnauthorizeRequest() throws ClientProtocolException, IOException {
        Client client = ClientFactory.create();
        client.resource(BASE_URL + "/api/resource/hello").get(String.class);
    }

    /**
     * This test will send authenticate with the server, change its token to
     * simulate the fact that the token has forgotten about it, and try another
     * call to check if negotiation happens again.
     */
    @Test
    public void testUnrecognizedToken() {
        // Given an authenticated service.
        final OAuth2Credentials credentials = new OAuth2Credentials(AUTHENTICATION_ENDPOINT, CLIENT_ID, CLIENT_SECRET,
                "test", "t3st");
        final Client client = OAuth2ClientFactory.create(credentials);
        final String serviceUrl = BASE_URL + "/api/resource/hello";
        String result = client.resource(serviceUrl).get(String.class);

        // When I change the access token with something the server can't
        // recognize, and I access the same service again.
        final String previousToken = credentials.getTokenResponse().accessToken;
        credentials.getTokenResponse().accessToken = previousToken + "RandomStringThatShouldMakeTheNextAuthFail";
        result = client.resource(serviceUrl).get(String.class);
        final String followingToken = credentials.getTokenResponse().accessToken;

        // Then we should have succeeded in our request, and the tokens should
        // be different.
        assertFalse("The token after renegotiation should be different", followingToken.equals(previousToken));
        assertEquals("Hello", result);
    }

    /**
     * This test will make sure that 2 calls in a row to the server keeps the
     * originating token.
     */
    @Test
    public void testMultipleCallsTokenPersistence() {
        // Given a call to a service.
        final OAuth2Credentials credentials = new OAuth2Credentials(AUTHENTICATION_ENDPOINT, CLIENT_ID, CLIENT_SECRET,
                "test", "t3st");
        final Client client = OAuth2ClientFactory.create(credentials);
        final String serviceUrl = BASE_URL + "/api/resource/hello";
        String result = client.resource(serviceUrl).get(String.class);
        String previousToken = credentials.getTokenResponse().accessToken;

        // When I call it again
        result = client.resource(serviceUrl).get(String.class);
        final String followingToken = credentials.getTokenResponse().accessToken;

        // Then the token should be identical
        assertEquals("The token shouldn't have changed", previousToken, followingToken);
    }
}
