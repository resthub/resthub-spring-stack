package org.resthub.core.monitoring;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.util.ReflectionUtils;

/**
 * Monitored Bean Processor.
 * 
 * @author Nicolas Carlier <nicolas.carlier@atos.net>
 */
public class MonitoringBeanPostProcessor implements BeanPostProcessor, Ordered {
	/** Quote */
	private static final String QUOTE = "'";
	
	/** Max size of a printed string. */
	private static final int MAX_STRING_LENGTH = 32;

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}

	@Override
	public Object postProcessBeforeInitialization(final Object bean, final String beanName) throws BeansException {
		final List<String> methodNames = new ArrayList<String>();
		if (bean.getClass().isAnnotationPresent(Monitored.class)) {
			ReflectionUtils.doWithMethods(bean.getClass(), new ReflectionUtils.MethodCallback() {
				public void doWith(Method method) {
					if (!method.isAnnotationPresent(NotMonitored.class)) {
						methodNames.add(method.getName());
					}
				}
			});
		}
		
		return (methodNames.size() > 0) ? createProxy(bean, methodNames) : bean;
	}

	@Override
	public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
		return bean;
	}
	
	
	private Object createProxy(Object target, List<String> methodNames) {
		final NameMatchMethodPointcut pc = new NameMatchMethodPointcut();
		pc.setMappedNames(methodNames.toArray(new String[]{}));
		
		final Advice advice = new MonitoredMethodInterceptor();
		final  Advisor advisor = new DefaultPointcutAdvisor(pc, advice);
		
		final ProxyFactory pf = new ProxyFactory();
		pf.addAdvisor(advisor);
		pf.setTarget(target);
		return pf.getProxy();
	}
	
	private class MonitoredMethodInterceptor implements MethodInterceptor {

        public Object invoke(MethodInvocation mi) throws Throwable {
			final Method method = mi.getMethod();
            final Object target = mi.getThis();
            final Object[] arguments = mi.getArguments();
			
			final Monitor mon = MonitorFactory.start(
				target.getClass().getName() + "." + method.getName()
			);
			boolean resultOk = true;
			Object result = null;
			
			final StringBuilder context = new StringBuilder();
			context.append("CALL: ");
			context.append(target.getClass().getSimpleName()).append(".");
			context.append(method.getName());
			
			// args
			context.append("(");
			final Object[] args = mi.getArguments();
			for (int i = 0; i < args.length; i++) {
				final Object o = args[i];
				if (o instanceof String) {
					String s = (String) o;
					if (s.length() > MAX_STRING_LENGTH) {
						s = s.substring(0, MAX_STRING_LENGTH);
						s = s.concat("...");
					}
					context.append(QUOTE).append(s).append(QUOTE);
				}
				else {
					context.append(o);
				}
				context.append((i == args.length - 1) ? "" : ", ");
			}
			context.append(")");
		
			final Logger logger = LoggerFactory.getLogger(target.getClass());
		
			if (logger.isDebugEnabled()) {
				logger.debug(context.toString());
			}
			try {
				result = method.invoke(target, arguments);
			} catch (Throwable e) {
				logger.error("*** INTERNAL ERROR: " + e.getCause().getMessage(), e);
				resultOk = false;
				throw e;
			} finally {
				mon.stop();
				if (resultOk) {
					context.append(" (OK)");
					if (logger.isDebugEnabled()) {
						logger.debug(context.toString());
					}
				} else {
					context.append("(ERR)");
					logger.error(context.toString());
				}
			}
			return result;
		}
	}
}
