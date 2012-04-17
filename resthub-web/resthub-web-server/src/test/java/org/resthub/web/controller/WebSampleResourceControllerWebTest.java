package org.resthub.web.controller;

import org.resthub.test.controller.AbstractControllerWebTest;
import org.resthub.web.model.WebSampleResource;

public class WebSampleResourceControllerWebTest extends AbstractControllerWebTest<WebSampleResource, Long> {

    @Override
    protected String getResourcePath() {
        return "/resources";
    }

    @Override
    protected WebSampleResource createTestResource() {
        return new WebSampleResource("toto");
    }

    @Override
    protected WebSampleResource udpateTestResource(WebSampleResource r) {
        r.setName("titi");
        return r;
    }

    @Override
    protected Long getResourceId(WebSampleResource resource) {
        return resource.getId();
    }

}
