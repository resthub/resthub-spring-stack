package org.resthub.web.test;

import org.hibernate.ObjectNotFoundException;
import org.resthub.test.AbstractWebTest;
import org.resthub.web.exception.*;
import org.resthub.web.model.Sample;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.testng.annotations.Test;

public class ExceptionMappingWebTest extends AbstractWebTest {
    
    public ExceptionMappingWebTest() {
         super("resthub-web-server,resthub-jpa");
    }
    
    @Test(expectedExceptions=NotAcceptableClientException.class)
    public void testHttpMediaTypeNotAcceptableException() {
        this.request("exception/test-default-spring-exception").jsonGet();
    }
    
    @Test(expectedExceptions=BadRequestClientException.class)
    public void testIllegalArgumentException() {
        this.request("exception/test-illegal-argument-exception").jsonGet();
    }
    
    @Test(expectedExceptions=InternalServerErrorClientException.class)
    public void testException() {
        this.request("exception/test-exception").jsonGet();
    }
    
    @Test(expectedExceptions=InternalServerErrorClientException.class)
    public void testRuntimeException() {
        this.request("exception/test-runtime-exception").jsonGet();
    }
    
    // Uncatched ClientEception should lead to an Internel Server Error, regardless the ClientException instance status code
    @Test(expectedExceptions=InternalServerErrorClientException.class)
    public void testClientException() {
        this.request("exception/test-client-exception").jsonGet();
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "test-object-not-found-exception")
    @ResponseBody
    public Sample throwObjectNotFoundException() {
        throw new ObjectNotFoundException("test", "test");
    }
    
    @Test(expectedExceptions=NotFoundClientException.class)
    public void testEntityNotFoundException() {
        this.request("exception/test-entity-not-found-exception").jsonGet();
    }
    
    @Test(expectedExceptions=ConflictClientException.class)
    public void testEntityExistsException() {
        this.request("exception/test-entity-exists-exception").jsonGet();
    }
    
}
