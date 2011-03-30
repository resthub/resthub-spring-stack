package org.resthub.identity.service;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.resthub.core.service.GenericResourceService;
import org.resthub.core.test.service.AbstractResourceServiceTest;
import org.resthub.identity.model.Group;

public class GroupServiceTest extends AbstractResourceServiceTest<Group, GenericResourceService<Group>> {

    Logger logger = Logger.getLogger(GroupServiceTest.class);

    @Override
    public Group createTestRessource() {
        String groupName = "GroupTestGroupName" + Math.round(Math.random() * 1000);
        Group g = new Group();
        g.setName(groupName);
        return g;
    }

    @Inject
    @Named("groupService")
    @Override
    public void setResourceService(GenericResourceService<Group> resourceService) {
        super.setResourceService(resourceService);
    }

    @Override
    @Test()
    public void testUpdate() throws Exception {
        /* Given a  new group*/
        String groupName = "GroupTestGroupUpdate";
        logger.debug("New group to be ask");
        Group g = new Group();
        logger.debug("New group asked");
        g.setName(groupName);
        logger.debug("creation will be asked");
        g = resourceService.create(g);
        logger.debug("Group with name" + g.getName() + "Created");
        String toString1 = "Group[Id: " + g.getId() + ", Name: " + g.getName() + "]";

        assertEquals(toString1, g.toString());

        String oldName = g.getName();
        String newName = "NewName";
        g.setName(newName);
        /* When we update the group after changing the name*/
        g = resourceService.update(g);

        /* the name modification is taken into account*/
        String toString2 = "Group[Id: " + g.getId() + ", Name: " + newName + "]";
        assertEquals(toString2, g.toString());

        resourceService.delete(g);
    }
}
