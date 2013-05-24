package org.resthub.jpa.specifications;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Persistable;
import org.springframework.util.NumberUtils;

public class DatePredicateFactory implements IPredicateFactory<Date> {

	public DatePredicateFactory() {
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
			Date date = new Date(NumberUtils.parseNumber(propertyValues[0], Long.class));
			predicate = cb.equal(root.<Date> get(propertyName), date);
		} else if (propertyValues.length == 2) {
			Date from = new Date(NumberUtils.parseNumber(propertyValues[0], Long.class));
			Date to = new Date(NumberUtils.parseNumber(propertyValues[1], Long.class));
			predicate = cb.between(root.<Date> get(propertyName), from, to);
		}
		return predicate;
	}
}