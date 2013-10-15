package org.resthub.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import javax.inject.Named;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Runner for @PostInitialize annotation registration
 *
 * @author AlphaCSP
 */
@SuppressWarnings("rawtypes")
@Named("postInitializerRunner")
public class PostInitializerRunner implements ApplicationListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostInitializerRunner.class);

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            LOGGER.debug("Scanning for Post Initializers...");
            long startTime = System.currentTimeMillis();
            ContextRefreshedEvent contextRefreshedEvent = (ContextRefreshedEvent) event;
            ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
            Map beans = applicationContext.getBeansOfType(Object.class, false, false);
            List<PostInitializingMethod> postInitializingMethods = new LinkedList<PostInitializingMethod>();
            for (Object beanNameObject : beans.keySet()) {
                String beanName = (String) beanNameObject;
                Object bean = beans.get(beanNameObject);
                if(bean == null) {
                    LOGGER.warn("Bean name {} return null, so we don't try to get matching bean", beanName);
                    continue;
                }
                Class<?> beanClass = bean.getClass();
                Method[] methods = beanClass.getMethods();
                for (Method method : methods) {
                    if (getAnnotation(method, PostInitialize.class) != null) {
                        if (method.getParameterTypes().length == 0) {
                            int order = getAnnotation(method, PostInitialize.class).order();
                            postInitializingMethods.add(new PostInitializingMethod(method, bean, order, beanName));
                        } else {
                            LOGGER.warn(
                                    "Post Initializer method can't have any arguments. {} in bean {} won't be invoked",
                                    method.toGenericString(), beanName);
                        }
                    }
                }
            }
            Collections.sort(postInitializingMethods);
            long endTime = System.currentTimeMillis();

            LOGGER.debug("Application Context scan completed, took {} ms, {} post initializers found. Invoking now.",
                    endTime - startTime, postInitializingMethods.size());
            for (PostInitializingMethod postInitializingMethod : postInitializingMethods) {
                Method method = postInitializingMethod.getMethod();
                try {
                    method.invoke(postInitializingMethod.getBeanInstance());
                } catch (IllegalAccessException e) {
                    throw new BeanCreationException("Post Initialization of bean "
                            + postInitializingMethod.getBeanName() + " failed.", e);
                } catch (IllegalArgumentException e) {
                    throw new BeanCreationException("Post Initialization of bean "
                            + postInitializingMethod.getBeanName() + " failed.", e);
                } catch (InvocationTargetException e) {
                    throw new BeanCreationException("Post Initialization of bean "
                            + postInitializingMethod.getBeanName() + " failed.", e);
                }
            }
        }
    }

    private <T extends Annotation> T getAnnotation(Method method, Class<T> annotationClass) {
        do {
            if (method.isAnnotationPresent(annotationClass)) {
                return method.getAnnotation(annotationClass);
            }
        } while ((method = getSuperMethod(method)) != null);
        return null;
    }

    @SuppressWarnings("unchecked")
    private Method getSuperMethod(Method method) {
        Class declaring = method.getDeclaringClass();
        if (declaring.getSuperclass() != null) {
            Class superClass = declaring.getSuperclass();
            try {
                Method superMethod = superClass.getMethod(method.getName(), method.getParameterTypes());
                if (superMethod != null) {
                    return superMethod;
                }
            } catch (NoSuchMethodException e) {
                return null;
            } catch (SecurityException e) {
                return null;
            }
        }
        return null;
    }

    private class PostInitializingMethod implements Comparable<PostInitializingMethod> {
        private Method method;
        private Object beanInstance;
        private int order;
        private String beanName;

        private PostInitializingMethod(Method method, Object beanInstance, int order, String beanName) {
            this.method = method;
            this.beanInstance = beanInstance;
            this.order = order;
            this.beanName = beanName;
        }

        public Method getMethod() {
            return method;
        }

        public Object getBeanInstance() {
            return beanInstance;
        }

        public String getBeanName() {
            return beanName;
        }

        @Override
        public int compareTo(PostInitializingMethod anotherPostInitializingMethod) {
            int thisVal = this.order;
            int anotherVal = anotherPostInitializingMethod.order;
            return (thisVal < anotherVal ? -1 : (thisVal == anotherVal ? 0 : 1));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            PostInitializingMethod that = (PostInitializingMethod) o;

            return order == that.order && !(beanName != null ? !beanName.equals(that.beanName) : that.beanName != null)
                    && !(method != null ? !method.equals(that.method) : that.method != null);
        }

        @Override
        public int hashCode() {
            int result;
            result = (method != null ? method.hashCode() : 0);
            result = 31 * result + (beanInstance != null ? beanInstance.hashCode() : 0);
            result = 31 * result + order;
            result = 31 * result + (beanName != null ? beanName.hashCode() : 0);
            return result;
        }
    }
}
