package org.resthub.web.log;

/**
 * User: JRI <julien.ripault@atos.net>
 * Date: 30/10/12
 */
public interface LogStrategy {
    void logError(LogDTO log);
    void logWarn(LogDTO log);
    void logInfo(LogDTO log);
    void logDebug(LogDTO log);
}
