package org.resthub.web.controller;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.resthub.web.model.WebSampleResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sun.jersey.api.NotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:resthubContext.xml",
		"classpath*:applicationContext.xml" })
public class WebSampleResourceControlleUpdateTest {

	@Inject
	@Named("webSampleResourceController")
	private WebSampleResourceController webSampleResourceController;

	@Test(expected = IllegalArgumentException.class)
	public void testUpdateWithIdNull() {
		WebSampleResource resource = new WebSampleResource();

		webSampleResourceController.update(null, resource);
	}

	@Test(expected = WebApplicationException.class)
	public void testUpdateWithDifferentIds() {
		WebSampleResource resource = new WebSampleResource();
		resource.setId(2L);

		webSampleResourceController.update(1L, resource);

	}

	@Test(expected = NotFoundException.class)
	public void testUpdate() {
		WebSampleResource resource = new WebSampleResource();
		resource.setName("test");

		webSampleResourceController.update(1L, resource);

	}

}
