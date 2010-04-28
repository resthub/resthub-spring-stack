package org.resthub.core.domain.dao;

import javax.inject.Named;

import org.resthub.core.domain.dao.jpa.GenericJpaResourceDao;
import org.resthub.core.domain.model.Resource;

@Named("resourceDao")
public class JpaResourceDao extends GenericJpaResourceDao<Resource> {

}
