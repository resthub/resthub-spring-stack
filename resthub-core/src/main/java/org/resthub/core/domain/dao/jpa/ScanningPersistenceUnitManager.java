package org.resthub.core.domain.dao.jpa;

import java.io.IOException;

import javax.persistence.Entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
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
import org.springframework.util.StringUtils;

public class ScanningPersistenceUnitManager extends
		DefaultPersistenceUnitManager {

	private final Logger log = LoggerFactory
			.getLogger(ScanningPersistenceUnitManager.class);

	protected String packagePatternToScan = null;

	public String getPackagePatternToScan() {
		return packagePatternToScan;
	}

	public void setPackagePatternToScan(String packagePatternToScan) {
		this.packagePatternToScan = packagePatternToScan;
	}

	@Override
	protected void postProcessPersistenceUnitInfo(MutablePersistenceUnitInfo pui) {

		if (this.packagePatternToScan != null) {

			String[] basePackages = StringUtils.tokenizeToStringArray(this.packagePatternToScan,
					ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);

			ResourcePatternResolver rpr = new PathMatchingResourcePatternResolver(Thread.currentThread().getContextClassLoader());
			TypeFilter entityTypeFilter = new AnnotationTypeFilter(Entity.class);
			MetadataReaderFactory metadataReaderFactory = new SimpleMetadataReaderFactory();
			Resource[] resources = new Resource[0];
			
			for (String basePackage : basePackages) {

				try {
					resources = rpr.getResources(basePackage);

					for (Resource resource : resources) {
						MetadataReader metadataReader = null;

						metadataReader = metadataReaderFactory.getMetadataReader(resource);
						if (entityTypeFilter.match(metadataReader, metadataReaderFactory)) {
							String entityName = metadataReader.getClassMetadata().getClassName();
							pui.addManagedClassName(entityName);
							if (log.isDebugEnabled()) {
								log.debug("Entity " + entityName + " found from package " + basePackage + ".");
							}
						}
					}
				} catch (IOException e) {
					log.warn("Error during scanning entities : cannot scan " + basePackage +".", e);
				}
				if (log.isInfoEnabled()) {
					log.info(basePackage+" successfully scanned : " + resources.length + " entities found.");
				}
			}
		}

		super.postProcessPersistenceUnitInfo(pui);
	}
}
