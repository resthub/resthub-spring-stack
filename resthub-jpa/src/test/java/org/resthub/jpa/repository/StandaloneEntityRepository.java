package org.resthub.jpa.repository;

import java.util.List;

import org.resthub.jpa.model.StandaloneEntity;
import org.springframework.data.jpa.repository.Query;

public interface StandaloneEntityRepository extends BaseRepository<StandaloneEntity, Long> {

    public List<StandaloneEntity> findByName(String name);

    public List<StandaloneEntity> findByNameLike(String name);

    @Query("from StandaloneEntity s where s.name = ?1")
    public List<StandaloneEntity> findByNameWithExplicitQuery(String name);
}
