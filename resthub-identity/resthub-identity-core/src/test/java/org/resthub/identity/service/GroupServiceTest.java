package org.resthub.identity.service;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.util.Random;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.resthub.core.service.GenericResourceService;
import org.resthub.core.test.service.AbstractResourceServiceTest;
import org.resthub.identity.model.Group;
import org.resthub.identity.model.User;
import org.resthub.identity.service.GroupService.GroupServiceChange;

public class GroupServiceTest extends AbstractResourceServiceTest<Group, GenericResourceService<Group>> {

    Logger logger = Logger.getLogger(GroupServiceTest.class);

    @Inject
    @Named("userService")
    private UserService userService;
    
    @PersistenceContext
    protected EntityManager em;

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

    @Test
    public void shouldDeleteGroupWithUsers() {
        /* Given a user */
        User testUser = new User();
        testUser.setLogin("testUser");
        testUser = userService.create(testUser);

        /* Given a group */
        Group testGroup  = this.createTestRessource();
        testGroup.setName("testGroup");
        testGroup = resourceService.create(testGroup);

        /* Given a link between this group and this user */
        userService.addGroupToUser(testUser.getLogin(), testGroup.getName());

        /* When deleting this group */
        resourceService.delete(testGroup);
        em.flush(); // enforce database queries to be executed

        /* Then the user shouldn't have this group anymore */
        User user = userService.findById(testUser.getId());
        assertFalse("The user shouldn't contain this group anymore", user.getGroups().contains(testGroup));
        /* And the group shouldn't exist */
        Group deleteGroup = resourceService.findById(testGroup.getId());
        assertNull("The deleted group shouldn't exist anymore", deleteGroup);
    }
    
    @Test
    public void shouldCreationBeNotified() {
    	// Given a registered listener
    	TestListener listener = new TestListener();
    	((GroupService)resourceService).addListener(listener);
    	
    	// Given a user
    	Group g = new Group();
    	g.setName("group"+new Random().nextInt());
    	
    	// When saving it
    	g = resourceService.create(g);
    	
    	// Then a creation notification has been received
    	assertEquals(GroupServiceChange.GROUP_CREATION.name(), listener.lastType);
    	assertArrayEquals(new Object[]{g}, listener.lastArguments);    	
    } // shouldCreationBeNotified().
    
    @Test
    public void shouldDeletionBeNotifiedById() {
    	// Given a registered listener
    	TestListener listener = new TestListener();
    	((GroupService)resourceService).addListener(listener);
    	
    	// Given a created group
    	Group g = new Group();
    	g.setName("group"+new Random().nextInt());
    	g = resourceService.create(g);
    	
    	// When removing it by id
    	resourceService.delete(g.getId());
    	
    	// Then a deletion notification has been received
    	assertEquals(GroupServiceChange.GROUP_DELETION.name(), listener.lastType);
    	assertArrayEquals(new Object[]{g}, listener.lastArguments);    	
    } // shouldDeletionBeNotifiedById().
    
    @Test
    public void shouldDeletionBeNotifiedByGroup() {
    	// Given a registered listener
    	TestListener listener = new TestListener();
    	((GroupService)resourceService).addListener(listener);
    	
    	// Given a created group
    	Group g = new Group();
    	g.setName("group"+new Random().nextInt());
    	g = resourceService.create(g);
    	
    	// When removing it
    	resourceService.delete(g);
    	
    	// Then a deletion notification has been received
    	assertEquals(GroupServiceChange.GROUP_DELETION.name(), listener.lastType);
    	assertArrayEquals(new Object[]{g}, listener.lastArguments);    	
    } // shouldDeletionBeNotifiedByGroup().
    
    @Test
    public void shouldGroupAdditionToGroupBeNotified() {
    	// Given a registered listener
    	TestListener listener = new TestListener();
    	((GroupService)resourceService).addListener(listener);
    	
    	// Given a created user
    	Group subG = new Group();
    	subG.setName("group"+new Random().nextInt());
    	subG = resourceService.create(subG);
    	
    	// Given a group
    	Group g = new Group();
    	g.setName("group"+new Random().nextInt());
    	g = resourceService.create(g);
    	
    	// When adding the user to the group
    	((GroupService)resourceService).addGroupToGroup(g.getName(), subG.getName());
    	
    	// Then a deletion notification has been received
    	assertEquals(GroupServiceChange.GROUP_ADDED_TO_GROUP.name(), listener.lastType);
    	assertArrayEquals(new Object[]{subG, g}, listener.lastArguments);    	
    } // shouldGroupAdditionToGroupBeNotified().
    
    @Test
    public void shouldGroupRemovalFromGroupBeNotified() {
    	// Given a registered listener
    	TestListener listener = new TestListener();
    	((GroupService)resourceService).addListener(listener);
    	
    	// Given a group
    	Group g = new Group();
    	g.setName("group"+new Random().nextInt());
    	g = resourceService.create(g);

    	// Given a created user in this group
    	Group subG = new Group();
    	subG.setName("group"+new Random().nextInt());
    	subG = resourceService.create(subG);
    	((GroupService)resourceService).addGroupToGroup(g.getName(), subG.getName());
   	
    	// When adding the user to the group
    	((GroupService)resourceService).removeGroupFromGroup(g.getName(), subG.getName());
    	
    	// Then a deletion notification has been received
    	assertEquals(GroupServiceChange.GROUP_REMOVED_FROM_GROUP.name(), listener.lastType);
    	assertArrayEquals(new Object[]{subG, g}, listener.lastArguments);    	
    } // shouldGroupRemovalFromGroupBeNotified().
}
