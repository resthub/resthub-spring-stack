package org.resthub.identity.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.resthub.identity.model.AbstractPermissionsOwner;
import org.resthub.identity.model.User;
import org.resthub.web.test.AbstractWebTest;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

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


    // -----------------------------------------------------------------------------------------------------------------
    // Tests
    @Test
    public void shouldIndexesBeReseted() {
        // Given a resource on the server
        WebResource server = resource();
        // When reseting indexes
        ClientResponse response = server.path("/api/search").put(ClientResponse.class);
        // Then the operation is processed
        assertEquals(204, response.getClientResponseStatus().getStatusCode());
    } // shouldIndexesBeReseted().

    @Test
    public void shouldNullQueryFailed() {
        // Given a resource on the server
        WebResource server = resource();
        // When searching without parameter
        ClientResponse response = server.path("/api/search").accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        // Then the result is an error.
        assertEquals(400, response.getClientResponseStatus().getStatusCode());
    } // shouldEmptyEmptyQueryReturnNothing().

    @Test
    public void shouldEmptyQueryFailed() {
        // Given a resource on the server
        WebResource server = resource();
        // When searching with empty query
        ClientResponse response = server.path("/api/search").queryParam("query", "").accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        // Then the result is empty.
        assertEquals(500, response.getClientResponseStatus().getStatusCode());
        String responseStr = response.getEntity(String.class);
        assertTrue(responseStr.contains("Misformatted queryString"));
    } // shouldEmptyQueryFailed().

    @Test
    public void shouldUnmatchingQueryReturnsEmptyResults() {
        // Given a resource on the server
        WebResource server = resource();
        // When searching with an unmatching query
        AbstractPermissionsOwner[] results = server.path("/api/search").queryParam("query", "toto").accept(MediaType.APPLICATION_JSON)
                .get(AbstractPermissionsOwner[].class);
        // Then the result is empty.
        assertNotNull(results);
        assertEquals(0, results.length);
    } // shouldUnmatchingQueryReturnsEmptyResults().

    @Test
    public void shouldQueryReturnsUsers() {
        // Given a resource on the server
        WebResource server = resource();
        // Given a user with jdujardin as login
        User user = new User();
        user.setLogin("jdujardin");
        user.setPassword("pwd");
        user = server.path("/api/user/").type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(User.class, user);

        // Given a user with jean as name
        User user2 = new User();
        user2.setLogin("user2");
        user2.setLastName("jean");
        user2.setPassword("pwd");
        user2 = server.path("/api/user/").type(MediaType.APPLICATION_JSON).post(User.class, user2);

        // When searching the created user
        User[] results = server.path("/api/search").queryParam("query", "j").accept(MediaType.APPLICATION_JSON).get(User[].class);
        // Then the result contains the user.
        assertNotNull(results);
        assertEquals(2, results.length);
        assertEquals(user, results[0]);
        assertEquals(user2, results[1]);

        // Cleanup after work
        server.path("/api/user/" + user.getId()).type(MediaType.APPLICATION_JSON).delete();
        server.path("/api/user/" + user2.getId()).type(MediaType.APPLICATION_JSON).delete();
    } // shouldQueryReturnsUsers().

    @Test
    public void shouldQueryReturnsUsersInJson() {
        // Given a resource on the server
        WebResource server = resource();
        // Given a user with jdujardin as login
        User user = new User();
        user.setLogin("jdujardin");
        user.setPassword("pwd");
        user = server.path("/api/user/").type(MediaType.APPLICATION_JSON).post(User.class, user);

        // Given a user with jean as name
        User user2 = new User();
        user2.setLogin("user2");
        user2.setLastName("jean");
        user2.setPassword("pwd");
        user2 = server.path("/api/user/").type(MediaType.APPLICATION_JSON).post(User.class, user2);

        ClientResponse response = server.path("/api/search").queryParam("query", "j")
                .accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        System.out.println(response.getEntity(String.class));

        // When searching the created user in JSON
        User[] results = server.path("/api/search").queryParam("query", "j").accept(MediaType.APPLICATION_JSON)
                .get(User[].class);
        // Then the result contains the user.
        assertNotNull(results);
        assertEquals(2, results.length);
        assertEquals(user, results[0]);
        assertEquals(user2, results[1]);

        // Cleanup after work
        server.path("/api/user/" + user.getId()).type(MediaType.APPLICATION_JSON).delete();
        server.path("/api/user/" + user2.getId()).type(MediaType.APPLICATION_JSON).delete();
    } // shouldQueryReturnsUsersInJson().

    @Test
    public void shouldQueryWithoutUsersNotReturnsUsers() {
        // Given a resource on the server
        WebResource server = resource();
        // Given a user
        User user = new User();
        user.setLogin("jdujardin");
        user.setPassword("pwd");
        user = server.path("/api/user/").type(MediaType.APPLICATION_JSON).post(User.class, user);

        // When searching the created user without users
        User[] results = server.path("/api/search").queryParam("query", "j").queryParam("users", "false")
        		.accept(MediaType.APPLICATION_JSON).get(User[].class);
        // Then the result does not contains the user.
        assertNotNull(results);
        assertEquals(0, results.length);

        // Cleanup after work
        server.path("/api/user/" + user.getId()).type(MediaType.APPLICATION_JSON).delete();
    } // shouldQueryReturnsUsers().
} // Class SearchControllerTest.
