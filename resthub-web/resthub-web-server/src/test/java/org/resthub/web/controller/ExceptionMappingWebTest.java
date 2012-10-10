/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.resthub.web.controller;

import org.fest.assertions.api.Assertions;
import org.resthub.test.common.AbstractWebTest;
import org.resthub.web.Client;
import org.resthub.web.Response;
import org.resthub.web.exception.NotAcceptableException;
import org.testng.annotations.Test;

/**
 *
 * @author Seb
 */
public class ExceptionMappingWebTest extends AbstractWebTest {
    
    public ExceptionMappingWebTest() {
         this.activeProfiles = "resthub-web-server,resthub-jpa";
    }
    
    protected String rootUrl() {
        return "http://localhost:" + port + "/repository-based";
    }
    
    @Test(expectedExceptions=NotAcceptableException.class)
    public void testCreateResource() {
        Client httpClient = new Client();
        httpClient.url(rootUrl()+"/test1").getJson();
    }
    
    
}
