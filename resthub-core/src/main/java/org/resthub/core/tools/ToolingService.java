package org.resthub.core.tools;

import java.util.List;

/**
 * Interface of the service used to retreive the current Spring application
 * context configuration
 */
public interface ToolingService {

    /**
     * @return the list of all bean names loaded in application context
     */
    List<String> getBeanNames();

    /**
     * @return the list of bean details for load beans
     */
    List<BeanDetail> getBeanDetails();

}
