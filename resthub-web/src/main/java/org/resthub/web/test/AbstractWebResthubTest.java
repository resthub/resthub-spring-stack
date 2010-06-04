package org.resthub.web.test;



import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.After;
import org.junit.Before;
import org.resthub.web.jackson.JacksonProvider;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.context.ContextLoaderListener;

public abstract class AbstractWebResthubTest {

    protected int port = 9797;
    protected Server server;

    @Before
    public void setUp() throws Exception {
        server = new Server(port);

        // On lance le serveur Jetty
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        context.setDisplayName("resthub test webapp");
        context.setContextPath("/");

        context.getInitParams().put("contextConfigLocation", "classpath*:resthubContext.xml classpath:resthubContext.xml classpath:applicationContext.xml");
        context.addFilter(OpenEntityManagerInViewFilter.class, "/*", 1);
        context.addServlet(SpringServlet.class, "/*");
        context.addEventListener(new ContextLoaderListener());

        server.setHandler(context);
        server.start();

    }

    @After
    public void tearDown() throws Exception {
        if (server != null) {
            server.stop();
        }
    }

    protected WebResource resource() {
        ClientConfig config = new DefaultClientConfig();
        config.getSingletons().add(new JacksonProvider());
        Client client = Client.create(config);
        return client.resource("http://localhost:" + port);
    }

}
