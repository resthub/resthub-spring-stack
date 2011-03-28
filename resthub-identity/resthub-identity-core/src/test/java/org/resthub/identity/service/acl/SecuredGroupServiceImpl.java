package org.resthub.identity.service.acl;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.identity.model.Group;
import org.resthub.identity.service.GroupService;

@Named("securedGroupService")
public class SecuredGroupServiceImpl implements SecuredGroupService {
	
	public GroupService getGroupService() {
		return groupService;
	}

	@Inject
	@Named("groupService")
	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}

	private GroupService groupService;
	
	/** Global role, because it does not exists **/
	public Group create(Group group) {
		return groupService.create(group);
	}
	
	public void delete(Group group) {
		groupService.delete(group);
	}

}
