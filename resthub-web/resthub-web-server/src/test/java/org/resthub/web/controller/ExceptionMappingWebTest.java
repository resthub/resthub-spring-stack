/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.resthub.web.controller;

import org.fest.assertions.api.Assertions;
import org.resthub.test.AbstractWebTest;
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
         super("resthub-web-server,resthub-jpa");
    }
    
    @Test(expectedExceptions=NotAcceptableClientException.class)
    public void testHttpMediaTypeNotAcceptableException() {
        this.request("exception/test-default-spring-exception").getJson();
    }
    
    @Test(expectedExceptions=BadRequestClientException.class)
    public void testIllegalArgumentException() {
        this.request("exception/test-illegal-argument").getJson();
    }
    
    @Test(expectedExceptions=InternalServerErrorClientException.class)
    public void testException() {
        this.request("exception/test-exception").getJson();
    }
    
    @Test(expectedExceptions=InternalServerErrorClientException.class)
    public void testRuntimeException() {
        this.request("exception/test-runtime-exception").getJson();
    }
    
    // Uncatched ClientEception should lead to an Internel Server Error, regardless the ClientException instance status code
    @Test(expectedExceptions=InternalServerErrorClientException.class)
    public void testClientException() {
        this.request("exception/test-client-exception").getJson();
    }
    
    
}
