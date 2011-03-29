package org.resthub.identity.service.acl;

import org.resthub.identity.model.Group;

public interface SecuredGroupService {
	
	/** Global role, because it does not exists **/
	Group create(Group group);
	
	void delete(Group group);

}
