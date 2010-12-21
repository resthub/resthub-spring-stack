package org.resthub.core.audit;

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

	/** Maximum argument output string length. */
	protected static final int MAX_STRING_LENGTH = 32;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Object invoke(MethodInvocation invocation) throws Throwable {
		return audit(invocation);
	}

	/**
	 * Perform audit around the invoked method
	 * 
	 * @param invocation
	 *            the method invocation joinpoint
	 * 
	 * @return the return object from the invoked method
	 * @throws Throwable
	 *             exception if one thrown
	 * @see {@link MethodInterceptor#invoke(MethodInvocation)}
	 */
	private final Object audit(MethodInvocation invocation) throws Throwable {

		Object result = null;
		Throwable error = null;

		this.preAudit(invocation);

		try {
			result = invocation.proceed();
		} catch (Throwable e) {
			error = e;
			this.auditError(invocation, error);
			throw e;
		} finally {
			postAudit(invocation, error);
		}
		return result;
	}

	/**
	 * Perform audit before method invocation.
	 */
	protected void preAudit(MethodInvocation invocation) {
		if (this.getTargetLogger(invocation).isDebugEnabled()) {
			this.getTargetLogger(invocation).debug("CALL: {}.{} ...",
					this.getClassName(invocation),
					this.getMethodCompleteSignature(invocation));
		}
	}

	/**
	 * Perform audit after method invocation. This method is called even an
	 * exception occured.
	 */
	protected void postAudit(MethodInvocation invocation, Throwable error) {
		if (this.getTargetLogger(invocation).isDebugEnabled()) {
			this.getTargetLogger(invocation).debug(
					"EXIT: {}.{} [" + ((error == null) ? "OK" : "ERR") + "]",
					this.getClassName(invocation),
					this.getMethodCompleteSignature(invocation));
		}
		
	}

	/**
	 * Perform audit when an error occured
	 */
	protected void auditError(MethodInvocation invocation, Throwable error) {
		Assert.notNull(error, "An error should have occured but it seems not!");
		this.getTargetLogger(invocation).error(
				"*** ERROR: Calling [{}.{}] fail with message ["
						+ error.getMessage() + "].",
				this.getClassName(invocation),
				this.getMethodCompleteSignature(invocation));
		if (error instanceof ConstraintViolationException) {
			logPreciseContraintViolation((ConstraintViolationException) error,
					invocation);

		}

		this.getTargetLogger(invocation).error("*** EXCEPTION:", error);
	}

	/**
	 * Build the logger from MethodInvocation object and its target class
	 */
	private Logger getTargetLogger(MethodInvocation invocation) {
		return LoggerFactory.getLogger(this
				.getTargetClassFromInvocation(invocation));
	}

	/**
	 * Specific method that allow to precisely perform audit from a
	 * {@link ConstraintViolationException}
	 */
	protected void logPreciseContraintViolation(ConstraintViolationException e,
			MethodInvocation invocation) {
		for (ConstraintViolation constraintViolation : e
				.getConstraintViolations()) {
			String[] violationDescriptor = {
					constraintViolation.getRootBeanClass().toString(),
					constraintViolation.getPropertyPath().toString(),
					constraintViolation.getMessage() };
			this
					.getTargetLogger(invocation)
					.error(
							"*** VALIDATION ERRORS:  [\n class: {} \n property: {} \n violation: {}",
							violationDescriptor);
		}
	}

	/**
	 * @param invocation
	 *            : method invocation context
	 * 
	 * @return a String representing the full signature of the method
	 */
	protected String getMethodCompleteSignature(MethodInvocation invocation) {
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
				signature.append("'").append(s).append("'");
			} else {
				signature.append(o);
			}

			signature.append((i == args.length - 1) ? "" : ", ");
		}
		signature.append(")");
		return signature.toString();
	}

	/**
	 * @param invocation
	 *            : method invocation context
	 * 
	 * @return the target class of this invocation context
	 */
	private Class<? extends Object> getTargetClassFromInvocation(
			MethodInvocation invocation) {
		return invocation.getThis().getClass();
	}

	/**
	 * @param invocation
	 *            : method invocation context
	 * 
	 * @return the target class of this invocation context
	 */
	protected String getClassName(MethodInvocation invocation) {
		final Class<?> targetClass = this
				.getTargetClassFromInvocation(invocation);
		return targetClass.getSimpleName();
	}
}
