package org.resthub.web.controller;

import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import com.sun.jersey.test.framework.spi.container.TestContainerFactory;
import com.sun.jersey.test.framework.spi.container.http.HTTPContainerFactory;

public abstract class AbstractWebResthubTest extends JerseyTest {

	public AbstractWebResthubTest() {
        super(new WebAppDescriptor.Builder("org.resthub.web.controller").
                contextPath("context").
                build());
    }
	
	@Override
    protected TestContainerFactory getTestContainerFactory() {
        return new HTTPContainerFactory();
    }
}
