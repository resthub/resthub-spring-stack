package org.resthub.web.oauth2;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Simple protected resource.
 */
@Controller @RequestMapping("/api/resource")
public class ResourceController {

    @RequestMapping(value= "ping", method = RequestMethod.GET) @ResponseBody
    public String sayPing() {
        return "Ping";
    }

    @RequestMapping(value= "hello", method = RequestMethod.GET) @ResponseBody
    public String sayHello() {
        return "Hello";
    }

    @RequestMapping(value= "goodbye", method = RequestMethod.GET) @ResponseBody
    public String sayGoodbye() {
        return "Goodbye";
    }
}