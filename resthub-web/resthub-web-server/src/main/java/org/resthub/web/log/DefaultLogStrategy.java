package org.resthub.web.log;

import java.text.SimpleDateFormat;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;

/**
 * Default log strategy
 */
@Named("defaultLogStrategy")
@Profile("resthub-client-logging")
public class DefaultLogStrategy implements LogStrategy {
    
    private static final Logger LOGGER = LoggerFactory.getLogger("JSLogger");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

    @Override
    public void logError(Log log) {
        LOGGER.error("[" + log.browser + "] " + DATE_FORMAT.format(log.time)  + " " + log.message);
    }

    @Override
    public void logWarn(Log log) {
        LOGGER.warn("[" + log.browser + "] " + DATE_FORMAT.format(log.time) + " " + log.message);
    }

    @Override
    public void logInfo(Log log) {
        LOGGER.info("[" + log.browser + "] " + DATE_FORMAT.format(log.time)  + " " + log.message);
    }

    @Override
    public void logDebug(Log log) {
        LOGGER.debug("[" + log.browser + "] " + DATE_FORMAT.format(log.time)  + " " + log.message);
    }
    
}
