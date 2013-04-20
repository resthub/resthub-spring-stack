package org.resthub.jpa.specifications;

import java.util.ArrayList;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Persistable;

public class EnumStringPredicateFactory implements IPredicateFactory<Enum> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EnumStringPredicateFactory.class);

	private Class type;

	private EnumStringPredicateFactory() {
	}

	public EnumStringPredicateFactory(Class clazz) {
		this.type = clazz;
	}


	/**
	 * @see org.resthub.jpa.specifications.IPredicateFactory#getPredicate(javax.persistence.criteria.Root,
	 *      javax.persistence.criteria.CriteriaBuilder, java.lang.String,
	 *      java.lang.Class, java.lang.String[])
	 */
	public Predicate getPredicate(Root<Persistable> root, CriteriaBuilder cb, String propertyName, Class fieldType,
			String[] propertyValues) {
		if (!fieldType.getClass().isEnum()) {
			LOGGER.warn("Non-Enum type for property '" + propertyName + "': " + fieldType.getName());
		}

		ArrayList<Enum> choices = new ArrayList<Enum>(propertyValues.length);
		for (int i = 0; i < propertyValues.length; i++) {
			try{
			choices.add(Enum.valueOf((Class<Enum>) this.type, propertyValues[i]));
			}catch(Exception e){
				LOGGER.warn(
						"Invalid Enum entry '" + propertyValues[i] + "' for property '" + propertyName + "' and class "
								+ fieldType.getName(), e);
			}
		}
		Expression<? extends Enum> exp = root.<Enum> get(propertyName);
		Predicate predicate = exp.in(choices.toArray());
		return predicate;
	}
}