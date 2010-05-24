package org.resthub.web.dao.jpa;

import javax.inject.Named;

import org.resthub.core.dao.GenericJpaResourceDao;
import org.resthub.web.dao.WebSampleResourceDao;
import org.resthub.web.model.WebSampleResource;

@Named("webSampleResourceDao")
public class WebSampleJpaResourceDao extends GenericJpaResourceDao<WebSampleResource> implements WebSampleResourceDao {

}
