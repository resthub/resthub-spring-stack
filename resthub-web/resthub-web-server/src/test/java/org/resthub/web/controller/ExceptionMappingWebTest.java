/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.resthub.web.controller;

import org.fest.assertions.api.Assertions;
import org.resthub.test.common.AbstractWebTest;
import org.resthub.web.Client;
import org.resthub.web.Response;
import org.resthub.web.exception.BadRequestClientException;
import org.resthub.web.exception.InternalServerErrorClientException;
import org.resthub.web.exception.NotAcceptableClientException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
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
        return "http://localhost:" + port + "/exception";
    }
    
    @Test(expectedExceptions=NotAcceptableClientException.class)
    public void testHttpMediaTypeNotAcceptableException() {
        Client httpClient = new Client();
        httpClient.url(rootUrl()+"/test-default-spring-exception").getJson();
    }
    
    @Test(expectedExceptions=BadRequestClientException.class)
    public void testIllegalArgumentException() {
        Client httpClient = new Client();
        httpClient.url(rootUrl()+"/test-illegal-argument").getJson();
    }
    
    @Test(expectedExceptions=InternalServerErrorClientException.class)
    public void testException() {
        Client httpClient = new Client();
        httpClient.url(rootUrl()+"/test-exception").getJson();
    }
    
    @Test(expectedExceptions=InternalServerErrorClientException.class)
    public void testRuntimeException() {
        Client httpClient = new Client();
        httpClient.url(rootUrl()+"/test-runtime-exception").getJson();
    }
    
    // Uncatched ClientEception should lead to an Internel Server Error, regardless the ClientException instance status code
    @Test(expectedExceptions=InternalServerErrorClientException.class)
    public void testClientException() {
        Client httpClient = new Client();
        httpClient.url(rootUrl()+"/test-client-exception").getJson();
    }
    
    
}
