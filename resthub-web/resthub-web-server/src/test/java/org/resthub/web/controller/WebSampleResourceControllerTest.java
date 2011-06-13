package org.resthub.web.controller;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Assert;
import org.junit.Test;
import org.resthub.web.model.WebSampleResource;
import org.resthub.web.test.controller.AbstractControllerTest;

public class WebSampleResourceControllerTest extends
        AbstractControllerTest<WebSampleResource, Long, WebSampleResourceController> {

    @Override
    @Inject
    @Named("webSampleResourceController")
    public void setController(WebSampleResourceController controller) {
        super.setController(controller);
    }

    @Test
    public void testUpdate() {
        WebSampleResource resource = controller.findById(this.id);
        String oldName = resource.getName();
        resource.setName("toto");
        WebSampleResource updatedResource = controller.update(this.id, resource);
        Assert.assertFalse(updatedResource.getName().equals(oldName));
    }

}
