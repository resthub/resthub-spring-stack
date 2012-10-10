package org.resthub.web;

import java.io.IOException;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

@Named("resthubHandlerExceptionResolver")
public class ResthubHandlerExceptionResolver implements HandlerExceptionResolver {

    protected Logger logger = LoggerFactory.getLogger(ResthubHandlerExceptionResolver.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            if (ex instanceof IllegalArgumentException) {
                return handleIllegalArgumentHandlingMethod((IllegalArgumentException) ex, request, response,
                        handler);
            }

        } catch (Exception handlerException) {
            logger.warn("Handling of [" + ex.getClass().getName() + "] resulted in Exception", handlerException);
        }
        return null;
    }

    protected ModelAndView handleIllegalArgumentHandlingMethod(IllegalArgumentException ex, HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        return new ModelAndView();
    }
}
