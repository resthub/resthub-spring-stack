package org.resthub.test.common;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.resthub.web.Http;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.servlet.DispatcherServlet;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClient.BoundRequestBuilder;

/**
 * Base class for your webservice tests, based on Jetty and with preconfigured
 * Spring configuration. Currently, a Jetty instance is launched for each tests
 * in order to reset test conditions. If you want to restart Jetty only once,
 * you can extend it, override setUp() methods and use @BeforeClass instead of @Before
 * annotations (don't forget to call super.setUp()) Ususally you will have to
 * redefine @ContextConfiguration to specify your application context file in
 * addition to resthub ones
 */
@RunWith(JUnit4.class)
public abstract class AbstractWebTest {

    protected int port = 9797;

    protected Server server;

    protected String contextLocations = "classpath*:resthubContext.xml classpath*:applicationContext.xml";
    protected String contextClass = "org.resthub.web.context.ResthubXmlWebApplicationContext";
    protected Boolean useOpenEntityManagerInViewFilter = false;
    protected int servletContextHandlerOption = ServletContextHandler.SESSIONS;
    
    protected static final Logger logger = Logger.getLogger(AbstractWebTest.class);

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Server getServer() {
        return this.server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public String getContextLocations() {
        return contextLocations;
    }

    public void setContextLocations(String contextLocations) {
        this.contextLocations = contextLocations;
    }

    /**
     * Allow you to customize context
     */
    protected ServletContextHandler customizeContextHandler(ServletContextHandler context) {
        return context;
    }

    @Before
    public void setUp() throws Exception {
        server = new Server(port);

        // Add a context for authorization service
        ServletContextHandler context = new ServletContextHandler(servletContextHandlerOption);
        context.getInitParams().put("contextClass", contextClass);
        context.getInitParams().put("contextConfigLocation", contextLocations);
        context.setDisplayName("resthub test webapp");
                        
        ServletHolder servletHolder = new ServletHolder(DispatcherServlet.class);
        servletHolder.setName("spring");
        servletHolder.setInitOrder(1);
        // Reuse beans detected by ContextLoaderListener so we configure an empty contextConfigLocation
        servletHolder.setInitParameter("contextConfigLocation", "");
        context.addServlet(servletHolder, "/");
        context.addEventListener(new ContextLoaderListener());
        
        if(useOpenEntityManagerInViewFilter) {
        	context.addFilter(OpenEntityManagerInViewFilter.class, "/*", EnumSet.of (DispatcherType.REQUEST));
        }

        // Starts the server.
        server.setHandler(context);

        server.start();

    }

    @After
    public void tearDown() {
        if (server != null) {
            try {
                server.stop();
            } catch (Exception e) {
                logger.error("Error while trying to stop embedded Jetty : " + e);
            }
        }
    }

    protected BoundRequestBuilder prepareGet(String path) {
        return new AsyncHttpClient().prepareGet("http://localhost:" + port + path).addHeader(Http.ACCEPT, Http.JSON); 
    }
    
    protected BoundRequestBuilder preparePost(String path) {
        return new AsyncHttpClient().preparePost("http://localhost:" + port + path).addHeader(Http.ACCEPT, Http.JSON).addHeader(Http.CONTENT_TYPE, Http.JSON); 
    }
    
    protected BoundRequestBuilder preparePut(String path) {
        return new AsyncHttpClient().preparePut("http://localhost:" + port + path).addHeader(Http.ACCEPT, Http.JSON).addHeader(Http.CONTENT_TYPE, Http.JSON); 
    }
    
    protected BoundRequestBuilder prepareDelete(String path) {
        return new AsyncHttpClient().prepareDelete("http://localhost:" + port + path).addHeader(Http.CONTENT_TYPE, Http.JSON); 
    }
    
    protected BoundRequestBuilder prepareGet() {
        return this.prepareGet(""); 
    }
    
    protected BoundRequestBuilder preparePost() {
    	return this.preparePost("");  
    }
    
    protected BoundRequestBuilder preparePut() {
    	return this.preparePut(""); 
    }
    
    protected BoundRequestBuilder prepareDelete() {
    	return this.prepareDelete(""); 
    }

}
