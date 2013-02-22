package org.resthub.web.controller;

import org.hibernate.ObjectNotFoundException;
import org.resthub.web.exception.ClientException;
import org.resthub.web.model.Sample;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

@Controller
@RequestMapping("/exception")
public class ExceptionController {
    
    @RequestMapping(method = RequestMethod.GET, value = "test-default-spring-exception")
    @ResponseBody
    public Sample throwDefaultHandledException() throws HttpMediaTypeNotAcceptableException {
        throw new HttpMediaTypeNotAcceptableException("test-default");
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "test-illegal-argument-exception")
    @ResponseBody
    public Sample throwIllegalArgumentException() throws IllegalArgumentException {
        throw new IllegalArgumentException();
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "test-runtime-exception")
    @ResponseBody
    public Sample throwRuntimeException() {
        throw new RuntimeException("test-default-runtime-exception");
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "test-exception")
    @ResponseBody
    public Sample throwDefaultException() throws Exception {
        throw new Exception("test-default-runtime-exception");
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "test-client-exception")
    @ResponseBody
    public Sample throwClientException() {
        throw new ClientException("test-client-exception");
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "test-object-not-found-exception")
    @ResponseBody
    public Sample throwObjectNotFoundException() {
        throw new ObjectNotFoundException("test", "test");
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "test-entity-not-found-exception")
    @ResponseBody
    public Sample throwEntityNotFoundException() {
        throw new EntityNotFoundException();
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "test-entity-exists-exception")
    @ResponseBody
    public Sample throwEntityExistsException() {
        throw new EntityExistsException();
    }
    
}
