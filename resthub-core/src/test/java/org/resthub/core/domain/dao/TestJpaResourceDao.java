package org.resthub.core.domain.dao;

import org.resthub.core.domain.dao.jpa.JpaResourceDao;
import org.resthub.core.domain.model.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class TestJpaResourceDao extends JpaResourceDao<Resource> {

}
