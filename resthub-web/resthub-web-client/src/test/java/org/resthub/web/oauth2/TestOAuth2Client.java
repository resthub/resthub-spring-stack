package org.resthub.web.oauth2;

import com.ning.http.client.AsyncHttpClientConfig.Builder;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.fest.assertions.api.Assertions;
import org.resthub.web.Client;
import org.resthub.web.Response;
import org.resthub.web.exception.UnauthorizedClientException;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

/**
* Tests the the TokenFactory utility class. Launches an in-memory jetty server with two contexts:
* <ol>
* <li>an authorization service build upon resthub-oauth2-provider.</li>
* <li>a resource service protected with resthub-oauth2-filter.</li>
* </ol>
*/
// TODO: refactor those tests - use mocks and avoid Thread.sleep
public class TestOAuth2Client {

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
        filterDef.setInitParameter("contextAttribute",
                "org.springframework.web.servlet.FrameworkServlet.CONTEXT.spring");
        context.addFilter(filterDef, "/*", EnumSet.of(DispatcherType.REQUEST));

        ServletHolder servletHolder = new ServletHolder(DispatcherServlet.class);
        servletHolder.setName("spring");
        servletHolder.setInitOrder(1);
        servletHolder.setInitParameter("contextConfigLocation",
                "classpath*:resthubContext.xml classpath*:applicationContext.xml");
        context.addServlet(servletHolder, "/");

        // Starts the server.
        server.setHandler(context);
        server.start();
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
    public void testOAuth2SuccessfulRequest() {
        Client client = new Client().setOAuth2("test", "t3st", ACCESS_TOKEN_ENDPOINT, CLIENT_ID, CLIENT_SECRET);
        String result = client.url(BASE_URL + "/api/resource/hello").get().getBody();
        Assertions.assertThat(result).isEqualTo("Hello");
    }

    @Test(expectedExceptions = {UnauthorizedClientException.class})
    public void testUnauthorizeRequest() {
        new Client().url(BASE_URL + "/api/resource/hello").jsonGet();
    }

    @Test
    public void testTokenExpired() throws InterruptedException {
        Client client = new Client().setOAuth2("test", "t3st", ACCESS_TOKEN_ENDPOINT, CLIENT_ID, CLIENT_SECRET);
        String result = client.url(BASE_URL + "/api/resource/hello").get().getBody();
        Assertions.assertThat(result).isEqualTo("Hello");

        // wait for the token to expire
        Thread.sleep(1000L);

        // the client should detect its token is expired and ask for a new token
        result = client.url(BASE_URL + "/api/resource/hello").get().getBody();
        Assertions.assertThat(result).isEqualTo("Hello");
    }
}
