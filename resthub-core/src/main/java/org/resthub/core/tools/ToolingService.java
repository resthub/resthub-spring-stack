package org.resthub.core.tools;

import java.util.List;


/**
 * @author Baptiste Meurant
 */
public interface ToolingService {
	
	/**
	 * @return the list of all bean names loaded in application context
	 */
	List<String> getBeanNames ();
	
	/**
	 * @return the list of bean details for load beans
	 */
	List<BeanDetail> getBeanDetails ();

}
