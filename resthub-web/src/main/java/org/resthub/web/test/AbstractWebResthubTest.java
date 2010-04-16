package org.resthub.web.test;

import org.springframework.web.context.ContextLoaderListener;

import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;

public abstract class AbstractWebResthubTest extends JerseyTest {

	public AbstractWebResthubTest() {
		super(new WebAppDescriptor.Builder()
        .contextPath("resthub")
        .contextParam("contextConfigLocation", "classpath*:hadesContext.xml classpath:hadesContext.xml classpath:resthubContext.xml classpath*:resthubContext.xml")
        .servletClass(SpringServlet.class)
        .contextListenerClass(ContextLoaderListener.class)
        .build());
    }	
	
}
