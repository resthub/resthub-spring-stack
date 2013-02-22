package org.resthub.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ExceptionResolver responsible for logging exception
 * @author Seb
 */
@Named("loggingHandlerExceptionResolver")
public class LoggingHandlerExceptionResolver implements HandlerExceptionResolver, Ordered {
    
    protected Logger logger = LoggerFactory.getLogger(LoggingHandlerExceptionResolver.class);
    
    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
        logger.warn("Exception catched by Spring MVC: " + e);
        return null; // trigger other HandlerExceptionResolver's
    }
    
}
