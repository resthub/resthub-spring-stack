package org.resthub.web;

import org.testng.annotations.Test;

/**
 * Test the HTTP Client
 */
public class ClientTest {

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testCreateClientWithQueryParamsInUrl() {
        Client client = new Client();
        client.url("http://example.org/resource?qparam=shouldfail");
    }
}
