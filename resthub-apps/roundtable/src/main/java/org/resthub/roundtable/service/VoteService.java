package org.resthub.roundtable.service;

import java.util.List;
import org.resthub.roundtable.domain.model.Poll;

/**
 * Vote services interface.
 * @author Nicolas Carlier (mailto:pouicbox@yahoo.fr)
 */
public interface VoteService {

    /**
     * Vote.
     * @param voter voter
     * @param pid Poll id
     * @param values values of each answers (ordered)
     * @return updated poll
     */
    Poll vote(String voter, Long pid, List<String> values);
    
}
