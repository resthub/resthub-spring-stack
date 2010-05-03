package org.resthub.core;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;
import org.aopalliance.intercept.MethodInvocation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    @Override
    public Object invoke(MethodInvocation invovation) throws Throwable {
        Object result = null;

        final Monitor mon = MonitorFactory.start(invovation.getMethod().getName());

        final StringBuilder context = new StringBuilder();
        context.append(invovation.getMethod().getName());

        context.append("(");
        final Object[] args = invovation.getArguments();
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
        final Logger logger = LoggerFactory.getLogger(invovation.getMethod().getDeclaringClass());

        if (logger.isDebugEnabled()) {
            logger.debug("CALL: {} ...", context.toString());
        }

        boolean error = false;
        try {
            result = invovation.proceed();
        } catch (Throwable e) {
            logger.error("*** ERROR: Calling [{}] fail with message [{}].", context.toString(), e.getMessage());
            logger.error("*** EXCEPTION:", e);
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
