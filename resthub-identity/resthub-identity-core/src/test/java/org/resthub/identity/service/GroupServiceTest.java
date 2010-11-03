package org.resthub.identity.service;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.resthub.core.service.GenericResourceService;
import org.resthub.core.test.service.AbstractResourceServiceTest;
import org.resthub.identity.model.Group;

public class GroupServiceTest extends AbstractResourceServiceTest<Group, GenericResourceService<Group>> {

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
		Group g=new Group();
		g=resourceService.create(g);
		
		String toString1="Group[Id: "+g.getId()+", Name: "+g.getName()+"]";

		assertEquals(toString1, g.toString());
		
		String oldName= g.getName();
		String newName="NewName";
		g.setName(newName);
		/* When we update the group after changing the name*/
		g=resourceService.update(g);
		
		/* the name modification is taken into account*/
		String toString2="Group[Id: "+g.getId()+", Name: "+newName+"]";
		assertEquals(toString2, g.toString());
						
		resourceService.delete(g);
    }
}
