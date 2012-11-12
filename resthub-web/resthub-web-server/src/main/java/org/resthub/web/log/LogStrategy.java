package org.resthub.web.log;

/**
 * Implement this interface to define the behaviour of the log controller
 */
public interface LogStrategy {
    void logError(Log log);
    void logWarn(Log log);
    void logInfo(Log log);
    void logDebug(Log log);
}
