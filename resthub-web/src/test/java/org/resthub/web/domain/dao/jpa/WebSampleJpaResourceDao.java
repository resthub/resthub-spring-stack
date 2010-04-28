package org.resthub.web.domain.dao.jpa;

import javax.inject.Named;

import org.resthub.core.domain.dao.jpa.GenericJpaResourceDao;
import org.resthub.web.domain.dao.WebSampleResourceDao;
import org.resthub.web.domain.model.WebSampleResource;

@Named("webSampleResourceDao")
public class WebSampleJpaResourceDao extends GenericJpaResourceDao<WebSampleResource> implements WebSampleResourceDao {

}
