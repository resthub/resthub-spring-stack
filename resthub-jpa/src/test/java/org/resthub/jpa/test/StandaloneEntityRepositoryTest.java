package org.resthub.jpa.test;

import org.fest.assertions.api.Assertions;
import org.resthub.jpa.model.StandaloneEntity;
import org.resthub.jpa.repository.StandaloneEntityRepository;
import org.resthub.test.AbstractTransactionalTest;
import org.springframework.test.context.ActiveProfiles;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ActiveProfiles("resthub-jpa")
public class StandaloneEntityRepositoryTest extends AbstractTransactionalTest {

    @Inject
    private StandaloneEntityRepository repository;

    @AfterMethod
    public void tearDown() {
        for (StandaloneEntity resource : repository.findAll()) {
            repository.delete(resource);
        }
    }

    @Test
    public void testSave() {
        StandaloneEntity entity = repository.save(new StandaloneEntity());
        Assertions.assertThat(repository.exists(entity.getId())).isTrue();
    }

    @Test
    public void testDelete() {
        StandaloneEntity entity = repository.save(new StandaloneEntity());
        Assertions.assertThat(repository.exists(entity.getId())).isTrue();

        repository.delete(entity);
        Assertions.assertThat(repository.exists(entity.getId())).isFalse();
    }

    @Test
    public void testDeleteById() {
        StandaloneEntity entity = repository.save(new StandaloneEntity());
        Assertions.assertThat(repository.exists(entity.getId())).isTrue();

        repository.delete(entity.getId());
        Assertions.assertThat(repository.exists(entity.getId())).isFalse();
    }

    @Test
    public void testFindAll() {
        List<StandaloneEntity> entities = new ArrayList<StandaloneEntity>(Arrays.asList(new StandaloneEntity(),
                new StandaloneEntity()));
        repository.save(entities);
        Assertions.assertThat(repository.count()).isEqualTo(entities.size());

        List<StandaloneEntity> collectedEntities = repository.findAll();
        Assertions.assertThat(collectedEntities).isNotNull().hasSize(entities.size());
    }

    @Test
    public void testFindById() {
        StandaloneEntity entity = repository.save(new StandaloneEntity());
        Assertions.assertThat(repository.exists(entity.getId())).isTrue();

        StandaloneEntity collectedEntity = repository.findOne(entity.getId());
        Assertions.assertThat(collectedEntity).isNotNull().isEqualTo(entity);
    }

    @Test
    public void testEncoding() {
        StandaloneEntity entity = repository.save(new StandaloneEntity("C\u2202dre"));
        Assertions.assertThat(repository.exists(entity.getId())).isTrue();

        StandaloneEntity collectedEntity = repository.findOne(entity.getId());
        Assertions.assertThat(collectedEntity).isNotNull().isEqualTo(entity);
    }

    @Test
    public void testFindByName() {
        String name = "testStandaloneEntity", name2 = "testStandaloneEntity2";
        List<StandaloneEntity> entities = new ArrayList<StandaloneEntity>(Arrays.asList(new StandaloneEntity(name),
                new StandaloneEntity(name)));
        repository.save(entities);
        Assertions.assertThat(repository.count()).isEqualTo(entities.size());

        StandaloneEntity entity = repository.save(new StandaloneEntity(name2));
        Assertions.assertThat(repository.exists(entity.getId())).isTrue();

        List<StandaloneEntity> collectedEntities = repository.findByName(name);
        Assertions.assertThat(collectedEntities).isNotNull().hasSize(entities.size());
    }

    @Test
    public void testFindByNameLike() {
        String name = "testStandaloneEntity", name2 = "testStandaloneEntity2", expression = name + "%";
        List<StandaloneEntity> entities = new ArrayList<StandaloneEntity>(Arrays.asList(new StandaloneEntity(name),
                new StandaloneEntity(name)));
        repository.save(entities);
        Assertions.assertThat(repository.count()).isEqualTo(entities.size());

        StandaloneEntity entity = repository.save(new StandaloneEntity(name2));
        Assertions.assertThat(repository.exists(entity.getId())).isTrue();

        List<StandaloneEntity> collectedEntities = repository.findByNameLike(expression);
        Assertions.assertThat(collectedEntities).isNotNull().hasSize((int) repository.count());
    }

    @Test
    public void testFindByNameWithExplicitQuery() {
        String name = "testStandaloneEntity", name2 = "testStandaloneEntity2";
        List<StandaloneEntity> entities = new ArrayList<StandaloneEntity>(Arrays.asList(new StandaloneEntity(name),
                new StandaloneEntity(name)));
        repository.save(entities);
        Assertions.assertThat(repository.count()).isEqualTo(entities.size());

        StandaloneEntity entity = repository.save(new StandaloneEntity(name2));
        Assertions.assertThat(repository.exists(entity.getId())).isTrue();

        List<StandaloneEntity> collectedEntities = repository.findByNameWithExplicitQuery(name);
        Assertions.assertThat(collectedEntities).isNotNull().hasSize(entities.size());
    }
}
