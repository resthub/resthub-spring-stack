package org.resthub.oauth2.filter.front;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URLEncoder;

import javax.ws.rs.core.HttpHeaders;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.resthub.oauth2.provider.service.MockAuthenticationService;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

/**
 * Check the OAuth2RediectionFilter functionnalities.
 */
public class OAuth2RedirectionFilterTest extends AbstractOAuth2FilterTest {

	// -----------------------------------------------------------------------------------------------------------------
	// Test suite initialization and finalization

	/**
	 * Before the test suite, launches a Jetty inmemory server.
	 */
	@BeforeClass
	public static void suiteSetUp() throws Exception {
		// Starts the resource server
		startResourceServer();
		// Starts the authorization server
		startAuthorizationServer();
	} // suiteSetUp().
	
	/**
	 * Creates a client that does not follow redirections 
	 */
	protected WebResource notFollowingResource() {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		client.setFollowRedirects(false);
		return client.resource("http://localhost:" + resourceServerPort +"/inmemory");
	} // notFollowingResource().
	
	// -----------------------------------------------------------------------------------------------------------------
	// Tests

	@Test
	public void shouldRequestBeRedirectedToAuthorizationServer() throws Exception {
		// Given a user-agent to request the protected server 
		WebResource server = notFollowingResource();
		
		// When accessing the redirection request with a requested uri.
		String requestedUri = "http://localhost:" + resourceServerPort +"/wantedUrl?param1=value&param2=value";
		ClientResponse response = server.path(redirectionFilterUrl)
			.queryParam("requested", requestedUri)
			.get(ClientResponse.class);
		
		// Then the user-agent is redirected
		assertEquals("Respons not redirected", 302, response.getStatus());
		assertTrue("No redirection url in response", response.getHeaders().containsKey(HttpHeaders.LOCATION));		
		String redirect = response.getHeaders().get(HttpHeaders.LOCATION).get(0);
		
		// Then redirection is to authorization server.
		assertTrue("Unexpected redirection url:" + redirect, redirect.startsWith("http://localhost:"+authorizationServerPort+
				authorizationServerRoot+"/api/authorize"));
		// Then the response_type parameter is code.
		assertTrue("No response_type parameter or wrong value: "+redirect, redirect.contains("response_type=code"));
		// Then the client_id is embedded.
		assertTrue("No client_id parameter or wrong value: "+redirect, redirect.contains("client_id="+resourceClientId));
		// Then the state is equal to the request uri.
		assertTrue("No state parameter or wrong value: "+redirect, redirect.contains("state="+
				URLEncoder.encode(requestedUri, "UTF-8")));
		
	} // shouldRequestBeRedirectedToAuthorizationServer().
	
	@Test
	public void shouldPostRequestFailOnError() {
		try {
			// When accessing by POST the redirection request
			resource().path(redirectionFilterUrl).post();
		} catch (UniformInterfaceException exc) {
			// Then an error is returned
			assertEquals("Unexpected error status", Status.METHOD_NOT_ALLOWED.getStatusCode(), 
					exc.getResponse().getStatus());
		}
	} // shouldPostRequestFailOnError().

	@Test
	public void shouldPutRequestFailOnError() {
		try {
			// When accessing by PUT the redirection request
			resource().path(redirectionFilterUrl).put();
		} catch (UniformInterfaceException exc) {
			// Then an error is returned
			assertEquals("Unexpected error status", Status.METHOD_NOT_ALLOWED.getStatusCode(), 
					exc.getResponse().getStatus());
		}
	} // shouldPutRequestFailOnError().

	@Test
	public void shouldDeleteRequestFailOnError() {
		try {
			// When accessing by PUT the redirection request
			resource().path(redirectionFilterUrl).get(ClientResponse.class);
		} catch (UniformInterfaceException exc) {
			// Then an error is returned
			assertEquals("Unexpected error status", Status.BAD_REQUEST.getStatusCode(), 
					exc.getResponse().getStatus());
		}
	} // shouldDeleteRequestFailOnError().

	@Test
	public void shouldMissingTargetUrlFailOnError() {
		try {
			// When accessing by PUT the redirection request
			resource().path(redirectionFilterUrl).delete();
		} catch (UniformInterfaceException exc) {
			// Then an error is returned
			assertEquals("Unexpected error status", Status.METHOD_NOT_ALLOWED.getStatusCode(), 
					exc.getResponse().getStatus());
		}
	} // shouldMissingTargetUrlFailOnError().

	@Test
	@Ignore
	// TODO : reactivate it when fixed
	public void shouldRequestedUriBedisplayedToKnownUserAfterAuthentication() throws Exception {
		// Given an access to the redirection request with a requested uri
		String requestedUri = "http://localhost:" + resourceServerPort +"/inmemory/admin";
		WebDriver driver = new HtmlUnitDriver();
        driver.get("http://localhost:" + resourceServerPort +"/inmemory"+redirectionFilterUrl+"?requested="+
        		URLEncoder.encode(requestedUri, "UTF-8"));
		// Given an authentication with a known user
        driver.findElement(By.name("username")).sendKeys("test");
        driver.findElement(By.name("password")).sendKeys("t3st");
        driver.findElement(By.id("authentSubmit")).submit();
		// Then the requested uri is displayed
        assertEquals("Client was not correctly redirected", requestedUri, driver.getCurrentUrl());
	} // shouldRequestedUriBedisplayedToKnownUserAfterAuthentication().

	@Test
	@Ignore
	// TODO : reactivate it when fixed
	public void shouldErrorBeDisplayedToUnknwonUserAfterAuthentication() throws Exception {
		// Given an access to the redirection request with a requested uri
		String requestedUri = "http://localhost:" + resourceServerPort +"/inmemory/admin";
		WebDriver driver = new HtmlUnitDriver();
        driver.get("http://localhost:" + resourceServerPort +"/inmemory"+redirectionFilterUrl+"?requested="+
        		URLEncoder.encode(requestedUri, "UTF-8"));		
		// Given an authentication with a unknown user
        driver.findElement(By.name("username")).sendKeys(MockAuthenticationService.UNKNOWN_USERNAME);
        driver.findElement(By.name("password")).sendKeys("t3st");
        driver.findElement(By.id("authentSubmit")).submit();
		
		// Then an error is displayed
        String url = driver.getCurrentUrl();
		assertTrue("No error parameter or wrong value: "+url, url.contains("error=access_denied"));
	} // shouldErrorBeDisplayedToUnknwonUserAfterAuthentication().
	
} // class OAuth2RedirectionFilterTest
