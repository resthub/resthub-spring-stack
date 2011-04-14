package org.resthub.core.context.config.jaxb;

import org.resthub.core.context.jaxb.JAXBElementListBean;
import org.resthub.core.context.jaxb.JAXBElementListIncluderBean;


/**
 * This class scan entities defined by a scanning configuration in application
 * context and add all found entities, matching with specified configuration
 * options, to the include list of persistence context in order to be managed
 * later (on initialization phasis)
 * 
 * @author bmeurant <Baptiste Meurant>
 */
public class IncludeJAXBElementsParser extends
		AbstractJAXBElementsParser {

	@Override
	protected Class<? extends JAXBElementListBean> getBeanClass() {
		return JAXBElementListIncluderBean.class;
	}

	@Override
	public String getElementName() {
		return "include-jaxb-elements";
	}
	
}
