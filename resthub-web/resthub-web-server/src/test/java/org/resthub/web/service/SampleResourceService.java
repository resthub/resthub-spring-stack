package org.resthub.web.service;

import org.resthub.common.service.CrudService;
import org.resthub.web.model.Sample;

/**
 * Generic Service interface.
 */
public interface SampleResourceService extends CrudService<Sample, Long> {
    
    Sample findByName(String name);

}
