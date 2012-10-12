package org.resthub.test.common;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.servlet.DispatcherServlet;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

/**
 * Base class for your webservice tests, based on Jetty and with preconfigured Spring configuration. Currently, a Jetty
 * instance is launched for each tests in order to reset test conditions. If you want to restart Jetty only once, you
 * can extend it, override setUp() methods and use @BeforeClass instead of @Before annotations (don't forget to call
 * super.setUp()) Ususally you will have to redefine @ContextConfiguration to specify your application context file in
 * addition to resthub ones
 */
public abstract class AbstractWebTest {

    protected int port = 9797;

    protected Server server;

    protected String contextLocations = "classpath*:resthubContext.xml classpath*:applicationContext.xml";
    protected String activeProfiles = "";
    protected Boolean useOpenEntityManagerInViewFilter = false;
    protected int servletContextHandlerOption = ServletContextHandler.SESSIONS;

    protected static final Logger logger = LoggerFactory.getLogger(AbstractWebTest.class);

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
    
    public String getActiveProfiles() {
        return activeProfiles;
    }

    public void setActiveProfiles(String activeProfiles) {
        this.activeProfiles = activeProfiles;
    }

    /**
     * Allow you to customize context
     */
    protected ServletContextHandler customizeContextHandler(ServletContextHandler context) {
        return context;
    }

    @BeforeClass
    public void beforeClass() throws Exception {
        server = new Server(port);

        // Add a context for authorization service
        ServletContextHandler context = new ServletContextHandler(servletContextHandlerOption);
        context.getInitParams().put("contextConfigLocation", contextLocations);
        context.getInitParams().put("spring.profiles.active", activeProfiles);
        
        ServletHolder defaultServletHolder = new ServletHolder(DefaultServlet.class);
        defaultServletHolder.setName("default");
        
        // Add default servlet in order to make <mvc:default-servlet-handler /> work in unit tests.
        // "/" servlet mapping will be overriden by dispatcher, but default servlet will stay in the context
        context.addServlet(defaultServletHolder, "/");

        ServletHolder dispatcherServletHolder = new ServletHolder(DispatcherServlet.class);
        dispatcherServletHolder.setName("dispatcher");
        dispatcherServletHolder.setInitOrder(1);
        // Reuse beans detected by ContextLoaderListener so we configure an
        // empty contextConfigLocation
        dispatcherServletHolder.setInitParameter("contextConfigLocation", "");
        context.addServlet(dispatcherServletHolder, "/");
        context.addEventListener(new ContextLoaderListener());

        if (useOpenEntityManagerInViewFilter) {
            context.addFilter(OpenEntityManagerInViewFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
        }

        // Starts the server.
        server.setHandler(context);

        server.start();
    }

    @AfterClass
    public void afterClass() {
        if (server != null) {
            try {
                server.stop();
            } catch (Exception e) {
                logger.error("Error while trying to stop embedded Jetty : " + e);
            }
        }
    }
}
