package org.resthub.tapestry5.jpa.services;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.tapestry5.ioc.services.RegistryShutdownListener;
import org.slf4j.Logger;
import org.tynamo.jpa.JPAEntityManagerSource;

/**
 * Define a service providing direct integration between a JPA entity manager
 * and Tapestry5 ioc and persistence services.
 * 
 * This service allows to create a JPAEntityManagerSource from an existing JPA
 * EntityManagerFActory (for example, already injectcted by spring)
 * 
 * @author bmeurant <Baptiste Meurant>
 * 
 */
public class DirectJPAEntityManagerSourceImpl implements
        JPAEntityManagerSource, RegistryShutdownListener {
    private final EntityManagerFactory entityManagerFactory;

    public DirectJPAEntityManagerSourceImpl(
            EntityManagerFactory entityManagerFactory) {

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
