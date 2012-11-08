package org.resthub.web.controller;

import org.resthub.web.model.Sample;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/annotation-config")
public class AnnotationConfigController {
    
    @RequestMapping(method = RequestMethod.GET, value = "test")
    @ResponseBody
    public Sample test() {
        return new Sample("toto");
    }
    
}
