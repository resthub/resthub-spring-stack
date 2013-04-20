package org.resthub.jpa.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Persistable;
import org.springframework.util.NumberUtils;

public class NumberPredicateFactory<T extends Number> implements IPredicateFactory<T> {

	private final Class<T> type;

	@SuppressWarnings("unused")
	private NumberPredicateFactory() {
		this.type = null;
	}

	public NumberPredicateFactory(Class<T> type) {
		this.type = type;
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
		if (!Number.class.isAssignableFrom(fieldType)) {
			throw new IllegalArgumentException(fieldType + " is not a subclass of Number for field: " + propertyName);
		}

		if (propertyValues.length == 1) {
			predicate = cb.equal(root.<T> get(propertyName), propertyValues[0]);
		} else if (propertyValues.length == 2) {
			T from = NumberUtils.parseNumber(propertyValues[0], this.type);
			T to = NumberUtils.parseNumber(propertyValues[1], this.type);
			Predicate predicate1 = cb.ge(root.<T> get(propertyName), from);
			Predicate predicate2 = cb.le(root.<T> get(propertyName), to);
			predicate = cb.and(predicate1, predicate2);
			// criteriaQuery.where(criteriaBuilder.and(predicate1,
			// predicate2));
			// predicate = cb.between(root.<T> get(propertyName), (Integer)
			// from, (Integer) to);
		}
		return predicate;
		// root...addStringSecification(personRoot, query,
		// cb, propertyName, searchTerms.get(propertyName));

	}
}