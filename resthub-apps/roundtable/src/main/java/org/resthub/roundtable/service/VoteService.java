package org.resthub.roundtable.service;

import java.util.List;

/**
 * Vote services interface.
 * 
 * @author Nicolas Carlier
 */
public interface VoteService {

    /**
     * Vote.
     * 
     * @param voterName
     *            voter name
     * @param pid
     *            Poll id
     * @param values
     *            values of each answers (ordered)
     */
    void vote(String voterName, Long pid, List<String> values);
}
