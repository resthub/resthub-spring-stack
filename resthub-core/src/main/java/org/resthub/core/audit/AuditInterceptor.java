package org.resthub.core.audit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.resthub.core.aop.AdvisorInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * Perform audit around a method invocation
 * 
 * @author Nicolas Carlier
 * @author bmeurant <Baptiste Meurant>
 */
@SuppressWarnings("serial")
public class AuditInterceptor extends AdvisorInterceptor {

	/** Quote */
	protected static final String QUOTE = "'";

	/** Maximum argument output string length. */
	protected static final int MAX_STRING_LENGTH = 32;

	private Logger logger;
	private List<Object> context;
	private Throwable occuredError;
	private MethodInvocation invocation;

	/**
	 * @return the parameters list of this context invocation
	 */
	protected final List<Object> getContext() {
		return context;
	}

	/**
	 * @return the logger for the target class
	 */
	protected final Logger getLogger() {
		return logger;
	}

	/**
	 * @return the error if one occured, null otherwise
	 */
	protected final Throwable getOccuredError() {
		return occuredError;
	}

	/**
	 * @return true if an error uccured, false otherwise
	 */
	protected final Boolean hasErrorOccured() {
		return occuredError != null;
	}

	/**
	 * @return the invocation context
	 */
	protected final MethodInvocation getInvocation() {
		return invocation;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Object invoke(MethodInvocation invocation) throws Throwable {
		this.invocation = invocation;
		return audit();
	}

	/**
	 * Perform audit around the invoked method
	 * 
	 * @return the return object from the invoked method
	 * @throws Throwable
	 *             exception if one thrown
	 * @see {@link MethodInterceptor#invoke(MethodInvocation)}
	 */
	private final Object audit() throws Throwable {

		this.buildTargetLogger();
		this.buildContext();

		Object result = null;

		this.preAudit();

		try {
			result = invocation.proceed();
		} catch (Throwable e) {
			this.occuredError = e;
			this.auditError();
			throw e;
		} finally {
			postAudit();
		}
		return result;
	}

	/**
	 * Perform audit before method invocation.
	 */
	protected void preAudit() {
		if (logger.isDebugEnabled()) {
			logger.debug("CALL: {}.{} ...", context.toArray());
		}
	}

	/**
	 * Perform audit after method invocation. This method is called even an
	 * exception occured.
	 */
	protected void postAudit() {
		if (logger.isDebugEnabled()) {
			logger.debug("EXIT: {}.{} [" + ((hasErrorOccured()) ? "ERR" : "OK")
					+ "]", context.toArray());
		}
	}

	/**
	 * Perform audit when an error occured
	 */
	protected void auditError() {
		Assert.notNull(this.occuredError,
				"An error should have occured but it seems not!");
		logger.error("*** ERROR: Calling [{}.{}] fail with message ["
				+ occuredError.getMessage() + "].", context.toArray());
		if (occuredError instanceof ConstraintViolationException) {
			logPreciseContraintViolation((ConstraintViolationException) occuredError);

		}

		logger.error("*** EXCEPTION:", occuredError);
	}

	/**
	 * Build context parameters (class name, method signature) from
	 * MethodInvocation object
	 */
	protected void buildContext() {
		final Class<?> targetClass = this.getTargetClassFromInvocation();
		final String methodSignature = constructMethodCompleteSignature();
		final String className = targetClass.getSimpleName();

		this.context = new ArrayList<Object>(Arrays.asList(className,
				methodSignature));
	}

	/**
	 * Build the logger from MethodInvocation object and its target class
	 */
	private void buildTargetLogger() {
		this.logger = LoggerFactory.getLogger(this
				.getTargetClassFromInvocation());
	}

	/**
	 * Specific method that allow to precisely perform audit from a
	 * {@link ConstraintViolationException}
	 */
	@SuppressWarnings("unchecked")
	protected void logPreciseContraintViolation(ConstraintViolationException e) {
		for (ConstraintViolation constraintViolation : e
				.getConstraintViolations()) {
			String[] violationDescriptor = {
					constraintViolation.getRootBeanClass().toString(),
					constraintViolation.getPropertyPath().toString(),
					constraintViolation.getMessage() };
			logger
					.error(
							"*** VALIDATION ERRORS:  [\n class: {} \n property: {} \n violation: {}",
							violationDescriptor);
		}
	}

	/**
	 * @return a String representing the full signature of the method
	 */
	protected String constructMethodCompleteSignature() {
		final StringBuilder signature = new StringBuilder();
		signature.append(invocation.getMethod().getName());

		signature.append("(");
		final Object[] args = invocation.getArguments();
		for (int i = 0; i < args.length; i++) {
			final Object o = args[i];
			if (o instanceof String) {
				String s = (String) o;
				if (s.length() > AuditInterceptor.MAX_STRING_LENGTH) {
					s = s.substring(0, AuditInterceptor.MAX_STRING_LENGTH);
					s = s.concat("...");
				}
				signature.append("'").append(s).append(
						"'");
			} else {
				signature.append(o);
			}

			signature.append((i == args.length - 1) ? "" : ", ");
		}
		signature.append(")");
		return signature.toString();
	}

	/**
	 * @return the target class of this invocation context
	 */
	private Class<? extends Object> getTargetClassFromInvocation() {
		return invocation.getThis().getClass();
	}
}
