package org.resthub.jpa.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Persistable;

public class StringPredicateFactory implements IPredicateFactory<String> {

	private static final Logger LOGGER = LoggerFactory.getLogger(StringPredicateFactory.class);

	public StringPredicateFactory() {
	}


	/**
	 * @see org.resthub.jpa.specifications.IPredicateFactory#getPredicate(javax.persistence.criteria.Root,
	 *      javax.persistence.criteria.CriteriaBuilder, java.lang.String,
	 *      java.lang.Class, java.lang.String[])
	 */
	public Predicate getPredicate(Root<Persistable> root, CriteriaBuilder cb, String propertyName, Class fieldType,
			String[] propertyValues) {
		if (!fieldType.equals(String.class)) {
			LOGGER.warn("Non-String type for property '" + propertyName + "': " + fieldType.getName());
		}

		Predicate predicate = null;
		if (propertyValues.length == 1) {
			String val = propertyValues[0];
			// case insensitive like?
			if (val.contains("%")) {
				predicate = cb.like(cb.lower(root.<String> get(propertyName)), val.toLowerCase());
			} else {
				predicate = cb.equal(root.<String> get(propertyName), val);
			}
		} else {// if (propertyValues.length == 2) {
			// TODO: add support for IN with  more than two values
			predicate = cb.between(root.<String> get(propertyName), propertyValues[0], propertyValues[1]);
		}
		return predicate;
	}
}