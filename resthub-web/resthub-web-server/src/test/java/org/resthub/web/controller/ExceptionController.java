package org.resthub.web.controller;

import org.hibernate.ObjectNotFoundException;
import org.resthub.web.exception.ClientException;
import org.resthub.web.model.Sample;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/exception")
public class ExceptionController {
    
    @RequestMapping(method = RequestMethod.GET, value = "test-default-spring-exception")
    public Sample throwDefaultHandledException() throws HttpMediaTypeNotAcceptableException {
        throw new HttpMediaTypeNotAcceptableException("test-default");
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "test-illegal-argument-exception")
    public Sample throwIllegalArgumentException() throws IllegalArgumentException {
        throw new IllegalArgumentException();
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "test-runtime-exception")
    public Sample throwRuntimeException() {
        throw new RuntimeException("test-default-runtime-exception");
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "test-exception")
    public Sample throwDefaultException() throws Exception {
        throw new Exception("test-default-runtime-exception");
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "test-client-exception")
    public Sample throwClientException() {
        throw new ClientException("test-client-exception");
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "test-object-not-found-exception")
    public Sample throwObjectNotFoundException() {
        throw new ObjectNotFoundException("test", "test");
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "test-entity-not-found-exception")
    public Sample throwEntityNotFoundException() {
        throw new EntityNotFoundException();
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "test-entity-exists-exception")
    public Sample throwEntityExistsException() {
        throw new EntityExistsException();
    }
    
}
