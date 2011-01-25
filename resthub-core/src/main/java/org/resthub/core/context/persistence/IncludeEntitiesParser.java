package org.resthub.core.context.persistence;


/**
 * This class scan entities defined by a scanning configuration in application
 * context and add all found entities, matching with specified configuration
 * options, to the include list of persistence context in order to be managed
 * later (on initialization phasis)
 * 
 * @author bmeurant <Baptiste Meurant>
 */
public class IncludeEntitiesParser extends
		AbstractEntitesParser {

	@Override
	protected Class<? extends EntityListBean> getBeanClass() {
		return EntityListIncluderBean.class;
	}
	
}
