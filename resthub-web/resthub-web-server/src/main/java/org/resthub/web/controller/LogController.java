package org.resthub.web.controller;

import org.resthub.web.log.Log;
import org.resthub.web.log.LogStrategy;
import org.resthub.web.log.Logs;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Log controller for client logging
 */
@Controller
@Profile("resthub-client-logging")
public class LogController {

    private LogStrategy logStrategy;

    /**
     * You can inject another LogStrategy bean in order to customize log handling
     */
    @Inject @Named("defaultLogStrategy")
    public void setLogStrategy(LogStrategy logStrategy) {
        this.logStrategy = logStrategy;
    }

    /**
     * Single log handling<br />
     * REST webservice published : POST /api/log
     *
     * @param log the log sent by the client
     * @param userAgent user Agent sent by the cient in Header
     */
    @RequestMapping(value = "api/log", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void logAction(@RequestBody Log log, @RequestHeader("User-Agent") String userAgent) {
        log.browser = userAgent;
        switch (log.level) {
            case debug:
                logStrategy.logDebug(log);
                break;
            case info:
                logStrategy.logInfo(log);
                break;
            case warn:
                logStrategy.logWarn(log);
                break;
            case error:
                logStrategy.logError(log);
                break;
            default:
                break;
        }
    }

    /**
     * Multiple log handling<br />
     * REST webservice published : POST /api/logs
     *
     * @param logs An array of logs sent by the client
     * @param userAgent user Agent sent by the cient in Header
     */
    @RequestMapping(value = "api/logs", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void log(@RequestBody Logs logs, @RequestHeader("User-Agent") String userAgent) {
        for (Log log : logs) {
            logAction(log, userAgent);
        }
    }
}
