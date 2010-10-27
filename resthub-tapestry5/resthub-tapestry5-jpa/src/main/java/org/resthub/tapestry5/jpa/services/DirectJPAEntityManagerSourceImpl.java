package org.resthub.tapestry5.jpa.services;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.tapestry5.ioc.services.RegistryShutdownListener;
import org.slf4j.Logger;
import org.tynamo.jpa.JPAEntityManagerSource;

public class DirectJPAEntityManagerSourceImpl implements JPAEntityManagerSource, RegistryShutdownListener {
	private final EntityManagerFactory entityManagerFactory;

	public DirectJPAEntityManagerSourceImpl(Logger logger, EntityManagerFactory entityManagerFactory) {

		this.entityManagerFactory = entityManagerFactory;

	}

	public EntityManager create() {
		return entityManagerFactory.createEntityManager();
	}

	public EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}

	public void registryDidShutdown() {
		entityManagerFactory.close();
	}
}

