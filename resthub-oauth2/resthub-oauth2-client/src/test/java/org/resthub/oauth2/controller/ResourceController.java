package org.resthub.oauth2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Simple protected resource.
 */
@Controller @RequestMapping("/api/resource")
public class ResourceController {

    @RequestMapping(value= "ping", method = RequestMethod.GET, produces = "application/json") @ResponseBody
    public String sayPing() {
        return "Ping";
    }

    @RequestMapping(value= "hello", method = RequestMethod.GET, produces = "application/json") @ResponseBody
    public String sayHello() {
        return "Hello";
    }

    @RequestMapping(value= "goodbye", method = RequestMethod.GET, produces = "application/json") @ResponseBody
    public String sayGoodbye() {
        return "Goodbye";
    }

}