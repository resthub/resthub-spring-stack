package org.resthub.core.tools;

import java.util.List;
import java.util.Map;

import org.resthub.core.service.ResourceService;


public interface ToolingService {
	
	List<String> getBeanNames ();
	
	Map<String, ResourceService> getResourceServiceBeans ();
	
	List<BeanDetail> getBeanDetails ();

}
