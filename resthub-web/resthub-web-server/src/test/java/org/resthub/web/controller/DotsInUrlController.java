package org.resthub.web.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dot")
public class DotsInUrlController {

    @RequestMapping(method = RequestMethod.GET, value = "{param}")
    public String getTest(@PathVariable String param) {
        return param;
    }

    @RequestMapping(method = RequestMethod.GET, value = "dot.{param}")
    public String getDotTest(@PathVariable String param) {
        return param;
    }
}
