package org.resthub.core.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.model.StandaloneEntity;
import org.resthub.core.repository.StandaloneEntityRepository;

/**
 * Dedicated interface to provide specific methods implementations for {@link StandaloneEntity} class.
 * 
 * This allows to validate that Resthub Generic Service manages entities
 */
@Named("standaloneEntityService")
public class StandaloneEntityServiceImpl extends GenericServiceImpl<StandaloneEntity, Long, StandaloneEntityRepository>
        implements StandaloneEntityService {

    /**
     * {@inheritDoc}
     */
    @Inject
    @Named("standaloneEntityRepository")
    @Override
    public void setRepository(StandaloneEntityRepository repository) {
        super.setRepository(repository);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getIdFromEntity(StandaloneEntity resource) {
        return resource.getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<StandaloneEntity> findByName(String name) {
        return this.repository.findByName(name);
    }
}
