package org.resthub.web.log;

import java.util.Date;

public class Log {
    public LogLevel level;
    public String message;
    public Date time;
    public String context;
    public String browser;

    public Log() {
    }
}
