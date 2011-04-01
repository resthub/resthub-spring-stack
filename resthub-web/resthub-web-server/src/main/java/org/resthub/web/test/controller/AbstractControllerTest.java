package org.resthub.web.test.controller;

import java.io.Serializable;
import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Test;
import org.resthub.core.service.GenericService;
import org.resthub.core.util.ClassUtils;
import org.resthub.web.controller.GenericController;
import org.resthub.web.test.AbstractWebResthubTest;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * Base class for your generic controller tests
 */
public abstract class AbstractControllerTest<T, PK extends Serializable, S extends GenericService<T, PK>, C extends GenericController<T, S, PK>>
						extends AbstractWebResthubTest {

	/**
	 * The tested controller
	 */
	protected C controller;

	/**
	 * Injection of controller
	 */
	public void setController(C controller) {
		this.controller = controller;
	}

	/**
	 * Returns the resource path of controller class
	 */
	public String getResourcePath() throws Exception {
		Class<? extends GenericController> classInstance = controller.getClass();
		return classInstance.getAnnotation(Path.class).value();
	}

	/**
	 * Returns the resource class
	 */
	public Class<?> getResourceClass() throws Exception {
		return this.createTestResource().getClass();
	}

	/**
	 * Creates a generic instance for tests
	 * @return T
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected T createTestResource() throws Exception {
		return (T) ClassUtils.getGenericTypeFromBean(this.controller).newInstance();
	}

	/**
	 * Cleans all persisted objects to simulate transactionnal tests
	 */
	@After
	public void cleanAll() throws Exception {
		List<T> list = controller.getService().findAll();
		for(T entity : list) {
			controller.delete(getIdFromObject(entity));
		}
	}

	/**
	 * Implement this method to return the primary key from its object.
	 * @param obj The object from whom we need primary key
	 * @return The corresponding primary key.
	 */
	protected abstract PK getIdFromObject(T obj);

	// -------------------------------------------------------------------------
	// Tests methods

	@Test
    public void testCreateResource() throws Exception {
        WebResource r = resource().path(getResourcePath());
		T res = (T)r.type(MediaType.APPLICATION_XML)
					.post(getResourceClass(), createTestResource());
		Assert.assertNotNull("Resource not created", res);
    }

	@Test
	public void testFindAllResourcesJson() throws Exception {
		WebResource r = resource().path(getResourcePath());
		r.type(MediaType.APPLICATION_XML).post(String.class, createTestResource());
		r.type(MediaType.APPLICATION_XML).post(String.class, createTestResource());
		String response = r.accept(MediaType.APPLICATION_JSON).get(String.class);

		Assert.assertTrue("Unable to find all resources or bad-formed JSON",
				response.contains("\"totalElements\":2"));
	}

	@Test
	public void testFindAllResourcesXml() throws Exception {
		WebResource r = resource().path(getResourcePath());
		r.type(MediaType.APPLICATION_XML).post(String.class, createTestResource());
		r.type(MediaType.APPLICATION_XML).post(String.class, createTestResource());
		String response = r.accept(MediaType.APPLICATION_XML).get(String.class);

		Assert.assertTrue("Unable to find all resources or bad-formed XML",
				response.contains("<totalElements>2</totalElements>"));
	}

	@Test
	public void testDeleteResource() throws Exception {
		WebResource r = resource().path(getResourcePath());
		T res = (T)r.type(MediaType.APPLICATION_XML)
					.accept(MediaType.APPLICATION_JSON)
					.post(getResourceClass(), createTestResource());
		Assert.assertNotNull("Resource not created", res);

		r = resource().path(getResourcePath() + "/" + getIdFromObject(res));
		ClientResponse response = r.delete(ClientResponse.class);
		Assert.assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatus());
		response = r.accept(MediaType.APPLICATION_XML).get(ClientResponse.class);
		Assert.assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
	}

	@Test
	public void testFindResource() throws Exception {
		WebResource r = resource().path(getResourcePath());
		T res = (T)r.type(MediaType.APPLICATION_XML)
					.post(getResourceClass(), createTestResource());
		Assert.assertNotNull("Resource not created", res);

		r = resource().path(getResourcePath() + "/" + getIdFromObject(res));
		ClientResponse cr = r.get(ClientResponse.class);
		Assert.assertEquals("Unable to find resource", Status.OK.getStatusCode(), cr.getStatus());
	}

	@Test
	public abstract void testUpdate() throws Exception;
}
