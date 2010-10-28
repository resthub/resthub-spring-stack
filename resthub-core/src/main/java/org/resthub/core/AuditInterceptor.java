package org.resthub.core;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

/**
 * Audit interceptor.
 * @author Nicolas Carlier
 */
@SuppressWarnings("serial")
public class AuditInterceptor extends AdvisorInterceptor {

	/** Quote */
    private static final String QUOTE = "'";

    /** Maximum argument output string length. */
    private static final int MAX_STRING_LENGTH = 32;

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
	@Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object result = null;
        
        final Monitor mon = MonitorFactory.start(invocation.getMethod().getName());

        final StringBuilder context = new StringBuilder();
        context.append(invocation.getMethod().getName());

        context.append("(");
        final Object[] args = invocation.getArguments();
        for (int i = 0; i < args.length; i++) {
            final Object o = args[i];
            if (o instanceof String) {
                String s = (String) o;
                if (s.length() > AuditInterceptor.MAX_STRING_LENGTH) {
                    s = s.substring(0, AuditInterceptor.MAX_STRING_LENGTH);
                    s = s.concat("...");
                }
                context.append(AuditInterceptor.QUOTE).append(s).append(AuditInterceptor.QUOTE);
            } else {
                context.append(o);
            }

            context.append((i == args.length - 1) ? "" : ", ");
        }
        context.append(")");

        // Logger
        final Logger logger = LoggerFactory.getLogger(invocation.getThis().getClass());

        if (logger.isDebugEnabled()) {
            logger.debug("CALL: {} ...", context.toString());
        }

        boolean error = false;
        try {
            result = invocation.proceed();
        } catch (Throwable e) {
            logger.error("*** ERROR: Calling [{}] fail with message [{}].", context.toString(), e.getMessage());
            if (e instanceof ConstraintViolationException) {
            	for (ConstraintViolation constraintViolation : ((ConstraintViolationException)e).getConstraintViolations()) {
            		String[] violationDescriptor = {constraintViolation.getRootBeanClass().toString(), constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage()};
            		logger.error("*** VALIDATION ERRORS:  [\n class: {} \n property: {} \n violation: {}", violationDescriptor);
				}
            	
            }
            else {
            	logger.error("*** EXCEPTION:", e);
            }
            error = true;
            throw e;
        } finally {
            if (logger.isDebugEnabled()) {
                logger.debug("EXIT: {} [{}]", context.toString(), (error) ? "ERR" : "OK");
            }
            mon.stop();
        }
        return result;
    }
}
