package org.resthub.web.log;

import java.util.Date;

/**
 * User: JRI <julien.ripault@atos.net>
 * Date: 30/10/12
 */
public class Log {
    public LogLevel level;
    public String message;
    public Date time;
    public String context;
    public String browser;

    public Log() {
    }
}
