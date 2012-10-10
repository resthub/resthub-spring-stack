package org.resthub.web;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.resthub.web.controller.RepositoryBasedRestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * ExceptionResolver responsible for logging exception
 * @author Seb
 */
@Named("loggingHandlerExceptionResolver")
public class LoggingHandlerExceptionResolver implements HandlerExceptionResolver, Ordered {
    
    protected Logger logger = LoggerFactory.getLogger(LoggingHandlerExceptionResolver.class);
    
    @Override
    public int getOrder() {
        return Integer.MIN_VALUE; // we're first in line, yay!
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest aReq, HttpServletResponse aRes, Object aHandler, Exception e) {
        logger.warn("Exception catched by Spring MVC : " + e);
        return null; // trigger other HandlerExceptionResolver's
    }
    
}
