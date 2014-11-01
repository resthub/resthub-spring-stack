package org.resthub.web.controller;

import org.resthub.web.model.Sample;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/annotation-config")
public class AnnotationConfigController {
    
    @RequestMapping(method = RequestMethod.GET, value = "test")
    public Sample test() {
        return new Sample("toto");
    }
    
}
