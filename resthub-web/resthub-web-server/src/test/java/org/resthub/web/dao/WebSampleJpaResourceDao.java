package org.resthub.web.dao;

import javax.inject.Named;

import org.resthub.core.dao.GenericJpaDao;
import org.resthub.web.model.WebSampleResource;

@Named("webSampleResourceDao")
public class WebSampleJpaResourceDao extends GenericJpaDao<WebSampleResource, Long> implements WebSampleResourceDao {

}
