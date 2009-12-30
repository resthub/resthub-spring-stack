package org.resthub.web.controller;

import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.context.ContextLoaderListener;

import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import com.sun.jersey.test.framework.spi.container.TestContainerFactory;
import com.sun.jersey.test.framework.spi.container.grizzly.web.GrizzlyWebTestContainerFactory;

public abstract class AbstractWebResthubTest extends JerseyTest {

	public AbstractWebResthubTest() {
		super(new WebAppDescriptor.Builder("org.resthub.web.controller")
        .contextPath("resthub")
        .contextParam("contextConfigLocation", "classpath:resthubContext.xml classpath*:resthubContext.xml")
        .servletClass(SpringServlet.class)
        .contextListenerClass(ContextLoaderListener.class)
        .build());
		
    }
	
	@Override
    protected TestContainerFactory getTestContainerFactory() {
        return new GrizzlyWebTestContainerFactory();
    }

}
