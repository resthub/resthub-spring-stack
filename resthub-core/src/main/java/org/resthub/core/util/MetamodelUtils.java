package org.resthub.core.util;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.SingularAttribute;
import org.springframework.util.ReflectionUtils;

/**
 * 
 * @author Loïc Frering <loic.frering@gmail.com>
 */
public class MetamodelUtils<T, PK extends Serializable> {

	private final Metamodel metamodel;
	private final Class<T> domainClass;

	public MetamodelUtils(Class<T> domainClass, Metamodel metamodel) {
		this.metamodel = metamodel;
		this.domainClass = domainClass;
	}

	/**
	 * Automatically retrieve ID from entity instance.
	 * 
	 * @param obj
	 *            The object from whom we need primary key
	 * @return The corresponding primary key.
	 */
	@SuppressWarnings("unchecked")
	public PK getIdFromEntity(T obj) {
		EntityType<T> type = metamodel.entity(domainClass);
		SingularAttribute<? super T, ?> attribute = type.getId(type.getIdType()
				.getJavaType());
		return (PK) getMemberValue(attribute.getJavaMember(), obj);
	}

	/**
	 * Returns the value of the given {@link Member} of the given {@link Object}
	 * . . Implementation From Spring Data JPA.
	 * 
	 * @param member
	 * @param source
	 * @return
	 */
	protected Object getMemberValue(Member member, Object source) {

		if (member instanceof Field) {
			Field field = (Field) member;
			ReflectionUtils.makeAccessible(field);
			return ReflectionUtils.getField(field, source);
		} else if (member instanceof Method) {
			Method method = (Method) member;
			ReflectionUtils.makeAccessible(method);
			return ReflectionUtils.invokeMethod(method, source);
		}

		throw new IllegalArgumentException(
				"Given member is neither Field nor Method!");
	}
}