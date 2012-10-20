package org.resthub.jpa;

import java.io.IOException;
import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.ObjectNotFoundException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;


@Named("jpaHandlerExceptionResolver")
public class JpaHandlerExceptionResolver extends AbstractHandlerExceptionResolver {
    
    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            if (ex instanceof ObjectNotFoundException) {
                return handleObjectNotFound((ObjectNotFoundException) ex, request, response,
                        handler);
            } else if (ex instanceof EntityNotFoundException) {
                return handleEntityNotFound((EntityNotFoundException) ex, request, response, handler);
            } else if (ex instanceof EntityExistsException) {
                return handleEntityExists((EntityExistsException) ex, request, response, handler);
            }           
            
        } catch (Exception handlerException) {
            logger.error("Handling of [" + ex.getClass().getName() + "] resulted in Exception", handlerException);
        }
        return null;  // trigger other HandlerExceptionResolver's
    }
    
    protected ModelAndView handleObjectNotFound(ObjectNotFoundException ex, HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
        return new ModelAndView();
    }
    
    protected ModelAndView handleEntityNotFound(EntityNotFoundException ex, HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
        return new ModelAndView();
    }
    
    protected ModelAndView handleEntityExists(EntityExistsException ex, HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        response.sendError(HttpServletResponse.SC_CONFLICT);
        return new ModelAndView();
    }
    
}