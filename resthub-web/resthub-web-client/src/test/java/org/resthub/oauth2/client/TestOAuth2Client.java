package org.resthub.oauth2.client;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.AsyncHttpClientConfig.Builder;
import com.ning.http.client.Realm;
import com.ning.http.client.Response;
import java.io.IOException;
import java.util.EnumSet;
import java.util.concurrent.ExecutionException;
import javax.servlet.DispatcherType;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.fest.assertions.api.Assertions;
import org.resthub.web.Http;
import org.resthub.web.oauth2.OAuth2RequestFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

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
    public static final String ACCESS_TOKEN_ENDPOINT = BASE_URL + "/oauth/token";

    /**
     * Jetty memory server instance.
     */
    protected static Server server;
    protected static Builder builder; 

    /**
     * Before the test suite, launches a Jetty in memory server.
     */
    @BeforeTest
    public void beforeClass() throws Exception {
        // Creates a Jetty server.
        server = new Server(PORT);

        // Add a context for authorization service
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setDisplayName("resthub test webapp");
                
        FilterHolder filterDef = new FilterHolder(DelegatingFilterProxy.class);
        filterDef.setName("springSecurityFilterChain");
        filterDef.setInitParameter("contextAttribute", "org.springframework.web.servlet.FrameworkServlet.CONTEXT.spring");
        context.addFilter(filterDef, "/*", EnumSet.of (DispatcherType.REQUEST));
                
        ServletHolder servletHolder = new ServletHolder(DispatcherServlet.class);
        servletHolder.setName("spring");
        servletHolder.setInitOrder(1);
        servletHolder.setInitParameter("contextConfigLocation", "classpath*:resthubContext.xml classpath*:applicationContext.xml");
        context.addServlet(servletHolder, "/");

        // Starts the server.
        server.setHandler(context);
        server.start();

        // Initialize OAuth2 authenticator
        builder = new AsyncHttpClientConfig.Builder();
        builder.addRequestFilter(new OAuth2RequestFilter(ACCESS_TOKEN_ENDPOINT, CLIENT_ID, CLIENT_SECRET));
    }

    /**
     * After the test suite, stops the Jetty in memory server.
     */
    @AfterTest
    public void afterClass() throws Exception {
        if (server != null) {
            server.stop();
        }
    }

    @Test
    public void testOAuth2SuccessfulRequest() throws IOException, InterruptedException, ExecutionException {
    	
    	AsyncHttpClient client = new AsyncHttpClient(builder.build());
    	
        String result = client.prepareGet(BASE_URL + "/api/resource/hello").setRealm(new Realm.RealmBuilder().setPrincipal("test").setPassword("t3st").build()).execute().get().getResponseBody();
        Assertions.assertThat(result).isEqualTo("Hello");
    }

    @Test
    public void testUnauthorizeRequest() throws IOException, InterruptedException, ExecutionException {
    	AsyncHttpClient client = new AsyncHttpClient();
    	Response response = client.prepareGet(BASE_URL + "/api/resource/hello").execute().get();
    	Assertions.assertThat(response.getStatusCode()).isEqualTo(Http.UNAUTHORIZED);
    }

}
