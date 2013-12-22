package org.resthub.test;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.resthub.web.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.servlet.DispatcherServlet;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import javax.servlet.DispatcherType;
import javax.servlet.ServletException;
import java.util.EnumSet;

/**
 * Base class for your webservice tests, based on Jetty and with preconfigured Spring configuration. By default, a single
 * Jetty server is started for all tests so you should take care of cleanup your data after your tests.
 * <p/>
 * If you want restart Jetty server after each test class, set startServerOnce to false and eventually customize port (in order to
 * avoid Jetty server listening on the same port conflict) in the constructor.
 * <p/>
 * By default it use XML context configuration. In order to use @Configuration class, you should set this.annotationBasedConfig = true
 * and this.contextLocations = "org.mypackage"; in your class constructor.
 * <p/>
 * Usually you will have to redefine the Spring profiles used by your application by calling AbstractWebTest super constructor
 */
public abstract class AbstractWebTest {

    /**
     * HTTP port used for running web tests, default is 9797
     */
    protected int port = 9797;

    /**
     * Specify if the embedded Jetty server should run once (true, default value) or for each class (false)
     */
    protected Boolean startServerOnce = true;

    /**
     * Jetty reusable embedded server that will run your application
     */
    protected static Server reusableServer;

    /**
     * Class specific Jetty embedded server that will run your application
     */
    protected Server server;

    /**
     * RESThub http client used to send test requests
     */
    protected Client client;

    /**
     * Default rootUrl used to send requests
     */
    protected String rootUrl;

    /**
     * Default Spring contexts imported. You should not have to customize this one frequently, prefer using Spring 3.1
     * profiles in order to control configuration activation.
     * <p/>
     * If you use JavaConfig based configuration, use it to specify the package where belong your @Configuration classes
     */
    protected String contextLocations = "classpath*:resthubContext.xml classpath*:applicationContext.xml";

    /**
     * List of Spring profiles (use comma separator) to activate
     * For example "resthub-web-server,resthub-jpa"
     */
    protected String activeProfiles = "";

    /**
     * When set to true, activate Spring 3.1 JavaConfig based configuration, by setting context param contextClass to org.springframework.web.context.support.AnnotationConfigWebApplicationContext
     */
    protected Boolean annotationBasedConfig = false;

    /**
     * Default constructor
     */
    public AbstractWebTest() {
        client = new Client();
    }

    /**
     * Constructor allowing to specify Spring active profiles
     *
     * @param activeProfiles coma separated list of profiles
     */
    public AbstractWebTest(String activeProfiles) {
        this();
        this.activeProfiles = activeProfiles;
    }

    /**
     * Constructor allowing to specify HTTP port used to run the server
     *
     * @param port HTTP port used for running web tests, default is 9797
     */
    public AbstractWebTest(int port) {
        this();
        this.port = port;
    }

    /**
     * Constructor allowing to specify Spring active profiles and HTTP port used to run the server
     *
     * @param activeProfiles coma separated list of profiles
     * @param port           HTTP port used for running web tests, default is 9797
     */
    public AbstractWebTest(String activeProfiles, int port) {
        this(port);
        this.activeProfiles = activeProfiles;
    }

    /**
     * Define in OpenEntityManagerInViewFilter should be activated or not. Default to false
     */
    protected Boolean useOpenEntityManagerInViewFilter = false;

    protected int servletContextHandlerOption = ServletContextHandler.SESSIONS;

    protected static final Logger logger = LoggerFactory.getLogger(AbstractWebTest.class);

    /**
     * You should override it in order to customize the servlet context handler
     */
    protected ServletContextHandler customizeContextHandler(ServletContextHandler context) throws ServletException {
        return context;
    }

    /**
     * Send a request to the embedded test webserver
     *
     * @param urlSuffix The end of the request without starting /, for example "todo" urlSuffix will send a request to "http://localhost:9797/todo"
     * @return The requestHolder that will allow you to build and send the request
     */
    public Client.RequestHolder request(String urlSuffix) {
        return this.client.url("http://localhost:" + this.port + "/" + urlSuffix);
    }

    @BeforeClass
    public void beforeClass() throws Exception {
        if ((!this.startServerOnce) || (reusableServer == null)) {
            server = new Server(port);

            // Add a context for authorization service
            ServletContextHandler context = new ServletContextHandler(servletContextHandlerOption);
            if (this.annotationBasedConfig) {
                context.getInitParams().put("contextClass", "org.springframework.web.context.support.AnnotationConfigWebApplicationContext");
            }
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

            // Customize context then start the server.
            server.setHandler(customizeContextHandler(context));
            server.start();

            if (this.startServerOnce) {
                reusableServer = server;
            }
        }
    }

    @AfterClass
    public void afterClass() {
        if ((server != null) && (!this.startServerOnce)) {
            try {
                server.stop();
            } catch (Exception e) {
                logger.error("Error while trying to stop embedded Jetty: " + e);
            }
        }
    }
}
