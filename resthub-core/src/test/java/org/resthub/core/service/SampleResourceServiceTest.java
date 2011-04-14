package org.resthub.core.service;

import javax.inject.Inject;
import javax.inject.Named;

public class SampleResourceServiceTest {

    @Inject
    @Named("sampleResourceService")
    public void setResourceService(
            SampleResourceService resourceService) {
        // TODO Auto-generated method stub
        //super.setResourceService(resourceService);
    }
    
//    @Override
//    @Test(expected = UnsupportedOperationException.class)
//
//
//    public void testUpdate() throws Exception {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
}
