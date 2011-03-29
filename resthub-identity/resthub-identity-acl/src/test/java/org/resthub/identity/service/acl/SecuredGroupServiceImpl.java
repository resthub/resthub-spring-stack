package org.resthub.identity.service.acl;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.identity.model.Group;
import org.resthub.identity.service.GroupService;
import org.springframework.security.access.prepost.PreAuthorize;

@Named("securedGroupService")
public class SecuredGroupServiceImpl implements SecuredGroupService {
	
	@Inject
	@Named("groupService")
	protected GroupService groupService;
	
	@Override
	public Group getById(Long id) {
		return groupService.findById(id);
	}
	
	/** Global role, because it does not exists **/
	@PreAuthorize("hasRole('CREATE')")
	@Override
	public Group create(Group group) {
		return groupService.create(group);
	}
	
	@PreAuthorize("hasPermission(#group, 'CUSTOM')")
	@Override
	public void delete(Group group) {
		groupService.delete(group);
	}

}
