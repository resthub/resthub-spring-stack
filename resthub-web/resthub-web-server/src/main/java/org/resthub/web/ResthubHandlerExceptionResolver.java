package org.resthub.web;

import java.io.IOException;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.ValidationException;
import org.resthub.common.exception.NotFoundException;
import org.resthub.common.exception.NotImplementedException;
import org.resthub.web.exception.ClientException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

@Named("resthubHandlerExceptionResolver")
public class ResthubHandlerExceptionResolver extends AbstractHandlerExceptionResolver {

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            if (ex instanceof IllegalArgumentException) {
                return handleIllegalArgument((IllegalArgumentException) ex, request, response,
                        handler);
            } else if (ex instanceof ValidationException) {
                return handleValidation((ValidationException) ex, request, response, handler);
            } else if (ex instanceof NotFoundException) {
                return handleNotFound((NotFoundException) ex, request, response, handler);
            } else if (ex instanceof NotImplementedException) {
                return handleNotImplemented((NotImplementedException) ex, request, response, handler);
            }           
            

        } catch (Exception handlerException) {
            logger.warn("Handling of [" + ex.getClass().getName() + "] resulted in Exception", handlerException);
        }
        return null;  // trigger other HandlerExceptionResolver's
    }
    
    protected ModelAndView handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        return new ModelAndView();
    }
    
    protected ModelAndView handleValidation(ValidationException ex, HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        return new ModelAndView();
    }
    
    protected ModelAndView handleNotFound(NotFoundException ex, HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
        return new ModelAndView();
    }
    
    protected ModelAndView handleNotImplemented(NotImplementedException ex, HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
        return new ModelAndView();
    }    
    
}
