package org.resthub.identity.service.tracability;

/**
 * This interface defines required stuff to allow third-party systems to track
 * modifications on implementing classes
 */
public interface TracableService {

    /**
     * Adds a listener to this service. Uneffective if the listener was already
     * registered.
     * 
     * @param listener
     *            The added listener.
     */
    void addListener(ServiceListener listener);

    /**
     * Removes a listener from this service. Uneffective if the listener was not
     * registered.
     * 
     * @param listener
     *            The added listener.
     */
    void removeListener(ServiceListener listener);

} // interface TracableService
