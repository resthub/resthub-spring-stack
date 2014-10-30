package org.resthub.web.oauth2;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Simple protected resource.
 */
@RestController
@RequestMapping("/api/resource")
public class ResourceController {

    @RequestMapping(value = "ping", method = RequestMethod.GET)
    public String sayPing() {
        return "Ping";
    }

    @RequestMapping(value = "hello", method = RequestMethod.GET)
    public String sayHello() {
        return "Hello";
    }

    @RequestMapping(value = "goodbye", method = RequestMethod.GET)
    public String sayGoodbye() {
        return "Goodbye";
    }
}