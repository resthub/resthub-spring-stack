package org.resthub.web.controller;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.resthub.core.test.AbstractTransactionAwareTest;
import org.resthub.web.exception.BadRequestException;
import org.resthub.web.exception.NotFoundException;
import org.resthub.web.model.WebSampleResource;

public class WebSampleResourceControlleUpdateTest extends AbstractTransactionAwareTest {

    @Inject
    @Named("webSampleResourceController")
    private WebSampleResourceController webSampleResourceController;

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWithIdNull() {
        WebSampleResource resource = new WebSampleResource();

        webSampleResourceController.update(null, resource);
    }

    @Test(expected = BadRequestException.class)
    public void testUpdateWithDifferentIds() {
        WebSampleResource resource = new WebSampleResource();
        resource.setId(2L);

        webSampleResourceController.update(1L, resource);

    }

    @Test(expected = NotFoundException.class)
    public void testUpdate() {
        WebSampleResource resource = new WebSampleResource();
        resource.setName("test");
        resource.setId(1L);

        webSampleResourceController.update(1L, resource);

    }

}
