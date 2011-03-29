package org.resthub.identity.service.acl;

import org.resthub.identity.model.Group;

public interface SecuredGroupService {
	
	Group getById(Long id);

	Group create(Group group);
	
	void delete(Group group);

}
