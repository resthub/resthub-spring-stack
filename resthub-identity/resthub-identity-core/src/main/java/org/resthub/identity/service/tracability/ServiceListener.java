package org.resthub.identity.service.tracability;

/**
 * This interface defines method needed to be notified when modification occurs
 * on a service
 */
public interface ServiceListener {

    /**
     * Notification method, invoked by the service when something noticeable
     * happens.
     * 
     * @param type
     *            Notification type.
     * @param arguments
     *            Arguments, specific on each notifications.
     */
    void onChange(String type, Object... arguments);

} // interface ServiceListener
