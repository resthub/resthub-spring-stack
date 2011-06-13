package org.resthub.core.context.config.persistence;

import org.resthub.core.context.persistence.EntityListBean;
import org.resthub.core.context.persistence.EntityListIncluderBean;

/**
 * This class scan entities defined by a scanning configuration in application
 * context and add all found entities, matching with specified configuration
 * options, to the include list of persistence context in order to be managed
 * later (on initialization phasis)
 */
public class IncludeEntitiesParser extends AbstractEntitesParser {

    @Override
    protected Class<? extends EntityListBean> getBeanClass() {
        return EntityListIncluderBean.class;
    }

    @Override
    public String getElementName() {
        return "include-entities";
    }

}
