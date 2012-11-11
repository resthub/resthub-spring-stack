package org.resthub.web.controller;

import com.atos.util.LogDTO;
import com.atos.util.LogStrategy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * User: JRI <julien.ripault@atos.net>
 * Date: 30/10/12
 */
@Controller
public class LogController {

    private LogStrategy logStrategy;

    public void setLogStrategy(LogStrategy logStrategy) {
        this.logStrategy = logStrategy;
    }

    @RequestMapping(value = "/admin/log", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void log(@RequestBody LogDTO log, @RequestHeader("User-Agent") String userAgent) {
        log.browser = userAgent;
        switch (log.level) {
            case DEBUG:
                logStrategy.logDebug(log);
                break;
            case INFO:
                logStrategy.logInfo(log);
                break;
            case WARN:
                logStrategy.logWarn(log);
                break;
            case ERROR:
                logStrategy.logError(log);
                break;
            default:
                break;
        }
    }

    @RequestMapping(value = "/admin/logs", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void log(@RequestBody Logs logs, @RequestHeader("User-Agent") String userAgent) {
        for (LogDTO logDTO : logs) {
            log(logDTO, userAgent);
        }
    }
}
