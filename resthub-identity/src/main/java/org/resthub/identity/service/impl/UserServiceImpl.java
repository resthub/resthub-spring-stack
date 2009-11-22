package org.resthub.identity.service.impl;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.domain.dao.ResourceDao;
import org.resthub.core.service.impl.ResourceServiceImpl;
import org.resthub.identity.domain.model.User;
import org.resthub.identity.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ResourceServiceImpl<User, ResourceDao<User>> implements UserService {

	@Override
	@Inject
	@Named("userDao")
	public void setResourceDao(ResourceDao<User> resourceDao) {
		super.setResourceDao(resourceDao);
	}

}
