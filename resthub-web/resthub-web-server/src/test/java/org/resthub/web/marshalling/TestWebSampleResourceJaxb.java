package org.resthub.web.marshalling;

import org.resthub.web.model.WebSampleResource;
import org.resthub.web.test.jaxb.AbstractResourceJaxbTest;

public class TestWebSampleResourceJaxb extends AbstractResourceJaxbTest<WebSampleResource> {

	@Override
	protected WebSampleResource createTestResource() {
		WebSampleResource resource = new WebSampleResource("testResource");
		return resource;
	}
}