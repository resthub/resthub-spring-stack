package org.resthub.core.domain.dao.jpa;

import javax.inject.Named;

import org.resthub.core.domain.model.Resource;

@Named("resourceDao")
public class JpaResourceDao extends AbstractJpaResourceDao<Resource> {

}
