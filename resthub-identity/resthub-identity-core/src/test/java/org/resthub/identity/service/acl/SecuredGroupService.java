package org.resthub.identity.service.acl;

import org.resthub.identity.model.Group;
import org.springframework.security.access.prepost.PreAuthorize;

public interface SecuredGroupService {
	
	@PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
	public Group create(Group group);
	
	@PreAuthorize("hasPermission(#group, 'delete')")
	public void delete(Group group);

}
