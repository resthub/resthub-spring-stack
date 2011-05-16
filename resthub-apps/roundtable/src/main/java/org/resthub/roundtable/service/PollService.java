package org.resthub.roundtable.service;

import org.resthub.core.service.GenericService;
import org.resthub.roundtable.model.Poll;
import org.resthub.roundtable.service.common.ServiceException;
import org.synyx.hades.domain.Page;
import org.synyx.hades.domain.Pageable;

/**
 * Poll services interface.
 * @author Nicolas Carlier
 */
public interface PollService extends GenericService<Poll, Long> {

    /**
     * Find polls by full text query.
     * @param query query
     * @param pageable pageable
     * @return polls matches
     * @throws ServiceException if bad query
     */
    Page<Poll> find(String query, Pageable pageable) throws ServiceException;

    /**
     * Rebuild full index.
     */
    void rebuildIndex();
}
