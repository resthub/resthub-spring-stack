package org.resthub.jpa.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.data.domain.Persistable;

public class BooleanPredicateFactory implements IPredicateFactory<Boolean> {

	public BooleanPredicateFactory() {
	}

	/**
	 * @see org.resthub.jpa.specifications.IPredicateFactory#getPredicate(javax.persistence.criteria.Root,
	 *      javax.persistence.criteria.CriteriaBuilder, java.lang.String,
	 *      java.lang.Class, java.lang.String[])
	 */
	@Override
	public Predicate getPredicate(Root<Persistable> root, CriteriaBuilder cb, String propertyName, Class fieldType,
			String[] propertyValues) {
		Predicate predicate = null;
		if (!Boolean.class.isAssignableFrom(fieldType)) {
			throw new IllegalArgumentException(fieldType + " is not a subclass of Number for field: " + propertyName);
		}

		Boolean b = BooleanUtils.toBooleanObject(propertyValues[0]);
		if (b == null) {
			b = Boolean.FALSE;
		}

		predicate = cb.equal(root.<Boolean> get(propertyName), b);
		return predicate;
	}
}