package org.resthub.oauth2.client;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.resthub.core.context.ResthubXmlContextLoader;
import org.resthub.oauth2.httpclient.OAuth2Credentials;
import org.resthub.web.client.ClientFactory;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;

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
    public static final String AUTHENTICATION_ENDPOINT = BASE_URL + "/api/oauth/authorize";
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

        context.getInitParams().put("contextConfigLocation",
                "classpath*:resthubContext.xml classpath*:applicationContext.xml");
        FilterHolder filterDef = new FilterHolder(DelegatingFilterProxy.class);
        filterDef.setName("springSecurityFilterChain");
        context.addFilter(filterDef, "/*", 0);
        context.addEventListener(new ContextLoaderListener());
        context.addServlet(SpringServlet.class, "/api/*");

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


    	Client client = OAuth2ClientFactory.create(new OAuth2Credentials(AUTHENTICATION_ENDPOINT, CLIENT_ID, CLIENT_SECRET, "test", "t3st"));
    	String result = client.resource(BASE_URL + "/api/resource/hello").get(String.class);
    	assertEquals("Hello", result);
    }
    
    @Test(expected=UniformInterfaceException.class)
    public void testUnauthorizeRequest() throws ClientProtocolException, IOException {
    	Client client = ClientFactory.create();
    	client.resource(BASE_URL + "/api/resource/hello").get(String.class);
    }


}
