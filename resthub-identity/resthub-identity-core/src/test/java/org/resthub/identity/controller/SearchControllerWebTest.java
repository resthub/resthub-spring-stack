package org.resthub.identity.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.resthub.identity.model.AbstractPermissionsOwner;
import org.resthub.identity.model.User;
import org.resthub.web.Http;
import org.resthub.web.JsonHelper;
import org.resthub.web.test.AbstractWebTest;

import com.ning.http.client.Response;

/**
 */
public class SearchControllerWebTest extends AbstractWebTest {
    
    /**
     * Test class logger.
     */
    Logger logger = Logger.getLogger(SearchControllerWebTest.class);
    
    @Override
	public void setUp() throws Exception {
		this.useOpenEntityManagerInViewFilter = true;
		super.setUp();
	}

    @Test
    public void shouldIndexesBeReseted() throws InterruptedException, ExecutionException, IOException {
        
        Response response = preparePut("/api/search").execute().get();
        // Then the operation is processed
        assertEquals(Http.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void shouldNullQueryFailed() throws InterruptedException, ExecutionException, IOException {
        // When searching without parameter
        Response response = prepareGet("/api/search").execute().get();
        // Then the result is an error.
        assertEquals(Http.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void shouldEmptyQueryFailed() throws InterruptedException, ExecutionException, IOException {
        // When searching with empty query
        Response response = prepareGet("/api/search?query=").execute().get();
        // Then the result is empty.
        assertEquals(Http.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getResponseBody().contains("Misformatted queryString"));
    }

    @Test
    public void shouldUnmatchingQueryReturnsEmptyResults() throws InterruptedException, ExecutionException, IOException {
        // When searching with an unmatching query
    	Response response = prepareGet("/api/search?query=toto").execute().get();
    	AbstractPermissionsOwner[] results = JsonHelper.deserialize(response.getResponseBody(), AbstractPermissionsOwner[].class); 

    	// Then the result is empty.
        assertNotNull(results);
        assertEquals(0, results.length);
    }

    @Test
    public void shouldQueryReturnsUsers() throws InterruptedException, ExecutionException, IOException {
        // Given a user with jdujardin as login
        User user = new User();
        user.setLogin("jdujardin");
        user.setPassword("pwd");
        
        Response response = preparePost("/api/user/").setBody(JsonHelper.serialize(user)).execute().get();
        user = JsonHelper.deserialize(response.getResponseBody(), User.class); 

        // Given a user with jean as name
        User user2 = new User();
        user2.setLogin("user2");
        user2.setLastName("jean");
        user2.setPassword("pwd");
        response = preparePost("/api/user/").setBody(JsonHelper.serialize(user2)).execute().get();
        user2 = JsonHelper.deserialize(response.getResponseBody(), User.class);

        // When searching the created user
        response= prepareGet("/api/search?query=j").execute().get();
        User[] results = JsonHelper.deserialize(response.getResponseBody(), User[].class);
        // Then the result contains the user.
        assertNotNull(results);
        assertEquals(2, results.length);
        assertEquals(user, results[0]);
        assertEquals(user2, results[1]);

        // Cleanup after work
        prepareDelete("/api/user/" + user.getId()).execute().get();
        prepareDelete("/api/user/" + user2.getId()).execute().get();
    }

    @Test
    public void shouldQueryReturnsUsersInJson() throws InterruptedException, ExecutionException, IOException {
        // Given a user with jdujardin as login
        User user = new User();
        user.setLogin("jdujardin");
        user.setPassword("pwd");
        Response response = preparePost("/api/user/").setBody(JsonHelper.serialize(user)).execute().get();
        user = JsonHelper.deserialize(response.getResponseBody(), User.class); 

        // Given a user with jean as name
        User user2 = new User();
        user2.setLogin("user2");
        user2.setLastName("jean");
        user2.setPassword("pwd");
        response = preparePost("/api/user/").setBody(JsonHelper.serialize(user2)).execute().get();
        user2 = JsonHelper.deserialize(response.getResponseBody(), User.class);

        response = prepareGet("/api/search?query=j").execute().get();
        User[] results = JsonHelper.deserialize(response.getResponseBody(), User[].class);

        // Then the result contains the user.
        assertNotNull(results);
        assertEquals(2, results.length);
        assertEquals(user, results[0]);
        assertEquals(user2, results[1]);

        // Cleanup after work
        prepareDelete("/api/user/" + user.getId()).execute().get();
        prepareDelete("/api/user/" + user2.getId()).execute().get();
    }

    @Test
    public void shouldQueryWithoutUsersNotReturnsUsers() throws IllegalArgumentException, InterruptedException, ExecutionException, IOException {

        // Given a user
        User user = new User();
        user.setLogin("jdujardin");
        user.setPassword("pwd");
        Response response = preparePost("/api/user/").setBody(JsonHelper.serialize(user)).execute().get(); 
        user = JsonHelper.deserialize(response.getResponseBody(), User.class);

        // When searching the created user without users
        response = prepareGet("/api/search?query=j&users=false").execute().get();
        User[] results = JsonHelper.deserialize(response.getResponseBody(), User[].class);
        // Then the result does not contains the user.
        assertNotNull(results);
        assertEquals(0, results.length);

        // Cleanup after work
        prepareDelete("/api/user/" + user.getId()).execute().get();
    }
}
