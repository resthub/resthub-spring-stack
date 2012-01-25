package org.resthub.web.controller;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.tools.BeanDetail;
import org.resthub.core.tools.ToolingService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * BeanDetailsController publishes a REST webservices used to list all Spring
 * beans discovered by Spring based on the current classpath and application
 * context files.
 * 
 * Remember to disable this one in production !
 */
@Controller @RequestMapping(value="/beans")
public class BeanDetailsController {

    @Inject
    @Named("toolingService")
    private ToolingService toolingService;

    @RequestMapping(method = RequestMethod.GET ) @ResponseBody
    public List<BeanDetail> getResources() {
        return  this.toolingService.getBeanDetails();

    }

}
