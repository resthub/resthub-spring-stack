package org.resthub.identity.service;

import java.util.HashSet;
import java.util.Set;

import org.resthub.core.dao.GenericDao;
import org.resthub.core.service.GenericServiceImpl;
import org.resthub.identity.service.tracability.ServiceListener;
import org.resthub.identity.service.tracability.TracableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class to put common stuff for service traceability.
 *
 * @author "Nicolas Morel <nicolas.morel@atosorigin.com>"
 */
public abstract class AbstractTraceableServiceImpl<TEntity, TDao extends GenericDao<TEntity, Long>> extends GenericServiceImpl<TEntity, TDao, Long> implements TracableService {

    /**
     * Set of registered listeners
     */
    protected Set<ServiceListener> listeners = new HashSet<ServiceListener>();
    /**
     * Class logger
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * {@inheritDoc}
     */
    @Override
    public void addListener(ServiceListener listener) {
        // Adds a new listener if needed.
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeListener(ServiceListener listener) {
        // Removes a listener if existing.
        if (listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }

    /**
     * Sends a notification to every listernes registered.
     * Do not fail if a user thrown an exception (report exception in logs).
     *
     * @param type Type of notification.
     * @param arguments Notification arguments.
     */
    protected void publishChange(String type, Object... arguments) {
        for (ServiceListener listener : listeners) {
            try {
                // Sends notification to each known listeners
                listener.onChange(type, arguments);
            } catch (Exception exc) {
                // Log exception
                logger.warn("[publishChange] Cannot publish " + type + " changes", exc);
            }
        }
    } // publishChange().
}
