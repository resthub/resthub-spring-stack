package org.resthub.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/dot")
public class DotsInUrlController {

    @RequestMapping(method = RequestMethod.GET, value = "{param}")
    public @ResponseBody String getTest(@PathVariable String param) {
        return param;
    }

    @RequestMapping(method = RequestMethod.GET, value = "dot.{param}")
    public @ResponseBody String getDotTest(@PathVariable String param) {
        return param;
    }
}
