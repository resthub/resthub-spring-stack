package org.resthub.web.view;

import org.resthub.common.view.ResponseView;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Decorator that detects a declared {@link ResponseView}, and injects support if required
 * @author martypitt
 */
public class ViewInjectingReturnValueHandler implements
        HandlerMethodReturnValueHandler {

    private final HandlerMethodReturnValueHandler delegate;

    public ViewInjectingReturnValueHandler(HandlerMethodReturnValueHandler delegate)
    {
        this.delegate = delegate;
    }
    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return delegate.supportsReturnType(returnType);
    }

    @Override
    public void handleReturnValue(Object returnValue,
            MethodParameter returnType, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest) throws Exception {

        Class<?> viewClass = getDeclaredViewClass(returnType);
        if (viewClass != null)
        {
            returnValue = wrapResult(returnValue,viewClass);    
        }

        delegate.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
    }
    /**
     * Returns the view class declared on the method, if it exists.
     * Otherwise, returns null.
     * @param returnType
     * @return
     */
    private Class<?> getDeclaredViewClass(MethodParameter returnType) {
        ResponseView annotation = returnType.getMethodAnnotation(ResponseView.class);
        if (annotation != null)
        {
            return annotation.value();
        } else {
            return null;
        }
    }
    private Object wrapResult(Object result, Class<?> viewClass) {
        PojoView response = new PojoView(result, viewClass);
        return response;
    }
}