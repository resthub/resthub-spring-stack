package org.resthub.core.domain.model.test;

import org.resthub.core.domain.dao.jpa.AbstractJpaResourceDao;
import org.resthub.core.domain.model.Resource;
import org.springframework.stereotype.Repository;

@Repository("resourceDaoTest")
public class JpaResourceDaoTest extends AbstractJpaResourceDao<Resource> {
	
	public JpaResourceDaoTest( ) {
		super(Resource.class);
	}
}
