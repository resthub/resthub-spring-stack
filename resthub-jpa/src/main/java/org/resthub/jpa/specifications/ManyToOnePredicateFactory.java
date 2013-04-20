package org.resthub.jpa.specifications;

import java.io.Serializable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Persistable;

/**
 * Builds predicates for a root's many2one members that implement org.springframework.data.domain.Persistable
 */
public class ManyToOnePredicateFactory<T extends Serializable> implements IPredicateFactory<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ManyToOnePredicateFactory.class);

	public ManyToOnePredicateFactory() {
	}


	/**
	 * @see org.resthub.jpa.specifications.IPredicateFactory#getPredicate(javax.persistence.criteria.Root,
	 *      javax.persistence.criteria.CriteriaBuilder, java.lang.String,
	 *      java.lang.Class, java.lang.String[])
	 */
	public Predicate getPredicate(Root<Persistable> root, CriteriaBuilder cb, String propertyName, Class fieldType,
			String[] propertyValues) {
		if (!Persistable.class.isAssignableFrom(fieldType)) {
			LOGGER.warn("Non-Entity type for property '" + propertyName + "': " + fieldType.getName());
		}

		Predicate predicate = null;
		if (propertyValues.length > 0) {
			Path<T> parentId = root.<Persistable> get(propertyName).<T> get("id");
			predicate = cb.equal(parentId, propertyValues[0]);
		}
		return predicate;
	}
}