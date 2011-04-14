package org.resthub.core.context.config.persistence;

import org.resthub.core.context.persistence.EntityListBean;
import org.resthub.core.context.persistence.EntityListExcluderBean;


/**
 * This class scan entities defined by a scanning configuration in application
 * context and add all found entities, matching with specified configuration
 * options, to the exclude list of persistence context in order to be managed
 * later (on bean initialization phasis)
 * 
 * @author bmeurant <Baptiste Meurant>
 */
public class ExcludeEntitiesParser extends
		AbstractEntitesParser {

	@Override
	protected Class<? extends EntityListBean> getBeanClass() {
		return EntityListExcluderBean.class;
	}

	@Override
	public String getElementName() {
		return "exclude-entities";
	}

}
