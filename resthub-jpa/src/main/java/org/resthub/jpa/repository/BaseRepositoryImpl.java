package org.resthub.jpa.repository;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.resthub.common.model.ParameterMapBackedPageRequest;
import org.resthub.jpa.specifications.GenericSpecifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

/**
 * A convenient base repository implementation. this class extends Spring's SimpleJpaRepository 
 * mainly to add dynamic search support (when using org.resthub.common.model.ParameterMapBackedPageRequest)
 * @param <T> the domain type the repository manages
 * @param <ID> the type of the id of the entity the repository manages
 */
public class BaseRepositoryImpl<T, ID extends Serializable>
 extends SimpleJpaRepository<T, ID> implements
		BaseRepository<T, ID> {

	private static final Logger LOGGER = LoggerFactory.getLogger(BaseRepositoryImpl.class);

	private EntityManager entityManager;
	private Class<T> domainClass;

	// There are two constructors to choose from, either can be used.
	public BaseRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
		super(domainClass, entityManager);

		// This is the recommended method for accessing inherited class
		// dependencies.
		this.entityManager = entityManager;
		this.domainClass = domainClass;
	}

	protected Class<T> getDomainClass() {
		return domainClass;
	}

	@Override
	public T merge(T entity) {
		return this.entityManager.merge(entity);
	}

	@Override
	public void refresh(T entity) {
		this.entityManager.refresh(entity);
	}

	@Override
	public T saveAndRefresh(T entity) {
		entity = this.save(entity);
		this.entityManager.refresh(entity);
		return entity;
	}

	/**
	 * Create a dynamic search when the given Pageable is an instance of 
	 * org.resthub.common.model.ParameterMapBackedPageRequest
	 */
	@Override
	public Page<T> findAll(Pageable pageable) {
		if (pageable instanceof ParameterMapBackedPageRequest) {
			@SuppressWarnings("unchecked")
			// static methods do not support generics so a cast is required
			Specification<T> spec = (Specification<T>) GenericSpecifications.matchAll(getDomainClass(),
					((ParameterMapBackedPageRequest) pageable).getParameterMap());
			return super.findAll(spec, pageable);
		} else {
			return super.findAll(pageable);
		}
	}

}
