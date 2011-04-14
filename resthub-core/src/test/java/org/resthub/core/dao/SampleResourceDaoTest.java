package org.resthub.core.dao;

import javax.inject.Inject;
import javax.inject.Named;

public class SampleResourceDaoTest {

    @Inject
    @Named("sampleResourceDao")
    public void setResourceDao(SampleResourceDao resourceDao) {
        //super.setResourceDao(resourceDao);
    }

//    @Override
//    @Test(expected = UnsupportedOperationException.class)
//    public void testUpdate() throws Exception {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
}
