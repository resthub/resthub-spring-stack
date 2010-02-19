package org.resthub.core.domain.dao.jpa;

import java.io.IOException;

import javax.persistence.Entity;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.orm.jpa.persistenceunit.DefaultPersistenceUnitManager;
import org.springframework.orm.jpa.persistenceunit.MutablePersistenceUnitInfo;

public class ResthubPersistenceUnitManager extends
		DefaultPersistenceUnitManager {

	@Override
	protected void postProcessPersistenceUnitInfo(MutablePersistenceUnitInfo pui) {

		ResourcePatternResolver rpr = new PathMatchingResourcePatternResolver(Thread.currentThread().getContextClassLoader());
		TypeFilter entityTypeFilter = new AnnotationTypeFilter(Entity.class);
		MetadataReaderFactory metadataReaderFactory = new SimpleMetadataReaderFactory();

		try {
			Resource[] resources = rpr
					.getResources("classpath*:org/resthub/**/domain/model/*.class");

			for (Resource resource : resources) {
				MetadataReader metadataReader = null;

				metadataReader = metadataReaderFactory.getMetadataReader(resource);
				if (entityTypeFilter.match(metadataReader, metadataReaderFactory)) {
					pui.addManagedClassName(metadataReader.getClassMetadata().getClassName());
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		super.postProcessPersistenceUnitInfo(pui);
	}
}
