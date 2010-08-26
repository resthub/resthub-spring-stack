package org.resthub.core.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.resthub.core.model.SampleResource;
import org.resthub.core.test.dao.AbstractResourceDaoTest;

public class SampleResourceDaoTest extends AbstractResourceDaoTest<SampleResource, SampleResourceDao> {

    @Inject
    @Named("sampleResourceDao")
    @Override
    public void setResourceDao(SampleResourceDao resourceDao) {
        // TODO Auto-generated method stub
        super.setResourceDao(resourceDao);
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void testUpdate() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
