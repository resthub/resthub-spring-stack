package org.resthub.core.tools;

import java.util.List;


public interface ToolingService {
	
	List<String> getBeanNames ();
	
	List<BeanDetail> getBeanDetails ();

}
