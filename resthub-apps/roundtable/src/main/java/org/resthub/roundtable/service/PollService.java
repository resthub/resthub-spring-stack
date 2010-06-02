package org.resthub.roundtable.service;

import java.util.List;
import org.resthub.core.service.GenericResourceService;
import org.resthub.roundtable.model.Poll;
import org.resthub.roundtable.service.common.ServiceException;

/**
 * Poll services interface.
 * @author Nicolas Carlier
 */
public interface PollService extends GenericResourceService<Poll> {

    /**
     * Find polls by full text query.
     * @param query query
     * @return polls matches
     * @throws ServiceException if bad query
     */
    List<Poll> find(String query) throws ServiceException;
}
