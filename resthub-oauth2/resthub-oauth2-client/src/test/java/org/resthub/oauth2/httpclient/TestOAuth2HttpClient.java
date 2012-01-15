package org.resthub.oauth2.httpclient;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import com.sun.jersey.spi.spring.container.servlet.SpringServlet;

/**
 * Tests the the TokenFactory utility class. Launches an in-memory jetty server
 * with two contexts:
 * <ol>
 * <li>an authorization service build upon resthub-oauth2-provider.</li>
 * <li>a resource service protected with resthub-oauth2-filter.</li>
 * </ol>
 */
public class TestOAuth2HttpClient {

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

        // Initialize the credentials
    	DefaultHttpClient client = new DefaultHttpClient();
        client.getAuthSchemes().register(OAuth2SchemeFactory.SCHEME_NAME, new OAuth2SchemeFactory());
    	OAuth2Credentials credentials = new OAuth2Credentials(AUTHENTICATION_ENDPOINT, CLIENT_ID, CLIENT_SECRET, "test", "t3st");
    	client.getCredentialsProvider().setCredentials(new AuthScope("localhost", PORT), credentials);
    	client.addRequestInterceptor(new OAuth2Interceptor(credentials), 0);
    	
    	HttpGet helloRequest = new HttpGet(BASE_URL + "/api/resource/hello");
    	HttpResponse response = client.execute(helloRequest);
    	String resultEntity = EntityUtils.toString(response.getEntity());
    	assertEquals("Hello", resultEntity);
    }
    
    @Test
    public void testUnauthorizeRequest() throws ClientProtocolException, IOException {
    	// Initialize the credentials
    	DefaultHttpClient client = new DefaultHttpClient();
    	HttpGet helloRequest = new HttpGet(BASE_URL + "/api/resource/hello");
    	HttpResponse response = client.execute(helloRequest);
    	String resultEntity = EntityUtils.toString(response.getEntity());
    	assertFalse("Hello".equals(resultEntity));
    }
    
}
