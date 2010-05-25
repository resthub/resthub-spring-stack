package org.resthub.web.dao;

import javax.inject.Named;

import org.resthub.core.dao.GenericJpaResourceDao;
import org.resthub.web.dao.WebSampleResourceDao;
import org.resthub.web.model.WebSampleResource;

@Named("webSampleResourceDao")
public class WebSampleJpaResourceDao extends GenericJpaResourceDao<WebSampleResource> implements WebSampleResourceDao {

}
