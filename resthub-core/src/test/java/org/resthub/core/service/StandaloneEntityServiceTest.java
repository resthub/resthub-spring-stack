package org.resthub.core.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Before;
import org.junit.Test;
import org.resthub.core.model.StandaloneEntity;
import org.resthub.core.test.AbstractResthubTest;

public class StandaloneEntityServiceTest extends AbstractResthubTest {

    @Inject
    @Named("standaloneEntityService")
    private StandaloneEntityService standaloneEntityService;

    @Before
    public void setUp() throws Exception {

        StandaloneEntity standaloneEntity = new StandaloneEntity();
        standaloneEntity.setName("test");
        standaloneEntity = this.standaloneEntityService.create(standaloneEntity);
    }

    @Test
    public void testFindByName() throws Exception {
        List<StandaloneEntity> entities = this.standaloneEntityService.findByName("test");
        assertNotNull("entities should not be null", entities);
        assertFalse("entities should not empty", entities.isEmpty());
        assertTrue("entities size should be exactly 1", entities.size() == 1);
        assertEquals("entitie name should be 'test'", "test", entities.get(0).getName());
    }

  
}
