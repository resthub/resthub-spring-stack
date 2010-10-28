package org.resthub.identity.service;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.resthub.core.service.GenericResourceService;
import org.resthub.core.test.service.AbstractResourceServiceTest;
import org.resthub.identity.model.Group;
import org.resthub.identity.model.User;

public class GroupServiceTest extends AbstractResourceServiceTest<Group, GenericResourceService<Group>> {

	@Inject
	@Named("groupService")
	@Override
	public void setResourceService(GenericResourceService<Group> resourceService) {
		super.setResourceService(resourceService);
	}

	@Override
    @Test(/*expected = UnsupportedOperationException.class*/)
    public void testUpdate() throws Exception {
		
		
		User u=new User();
		String firstName="Maxence";
		u.setFirstName(firstName);

		String lastName="Dalmais";
		u.setLastName(lastName);
		
		Group g=new Group();
		g=resourceService.create(g);
		
		String toString1="Group[Id: "+g.getId()+", Name: "+g.getName()+"]";

		assertEquals(toString1, g.toString());
		
		String oldName= g.getName();
		String newName="NewName";
		g.setName(newName);
		g=resourceService.update(g);
		String toString2="Group[Id: "+g.getId()+", Name: "+newName+"]";
		assertEquals(toString2, g.toString());
						
    }
}
