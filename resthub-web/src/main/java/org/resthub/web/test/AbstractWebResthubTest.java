package org.resthub.web.test;

import org.springframework.web.context.ContextLoaderListener;

import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import org.resthub.web.jackson.JacksonProvider;

public abstract class AbstractWebResthubTest extends JerseyTest {

    @Override
    protected AppDescriptor configure() {
        WebAppDescriptor wad = new WebAppDescriptor.Builder().contextPath("resthub").contextParam("contextConfigLocation", "classpath*:resthubContext.xml classpath:resthubContext.xml classpath:applicationContext.xml").servletClass(SpringServlet.class).contextListenerClass(ContextLoaderListener.class).build();

        wad.getClientConfig().getSingletons().add(new JacksonProvider());

        return wad;
    }
}
