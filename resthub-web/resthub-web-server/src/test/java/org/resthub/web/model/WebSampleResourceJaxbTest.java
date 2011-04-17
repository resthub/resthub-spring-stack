package org.resthub.web.model;

import javax.xml.bind.JAXBException;

import junit.framework.Assert;

import org.junit.Test;
import org.resthub.web.test.AbstractJaxbTest;

public class WebSampleResourceJaxbTest extends AbstractJaxbTest {

	@Test
	public void testWebSampleResourceSerialization() throws JAXBException {
		WebSampleResource resource = new WebSampleResource("testResource");
		String output = this.serialize(resource);
		Assert.assertTrue(output.contains("testResource"));
	}
}