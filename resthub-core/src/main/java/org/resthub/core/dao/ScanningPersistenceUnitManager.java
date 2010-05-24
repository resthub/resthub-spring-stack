package org.resthub.core.dao;

import java.io.IOException;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

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

/**
 * Allow to scan JPA Entities in all dependencies (JAR) of the application, in order
 * to allow modular JPA applications whith a model classes distributed in various
 * modules.
 * 
 * By default with JPA, this is not possible within the same persistence unit, so we
 * implemented this using Spring classpath scanning capabilities.
 * 
 * Sample Spring configuration :
 * <pre>
 * {@code 
 * <bean id="scanningPersistenceUnitManager"
 *		class="org.resthub.core.domain.dao.jpa.ScanningPersistenceUnitManager">
 *		<property name="defaultDataSource" ref="dataSource" />
 *		<property name="classpathPatterns"
 *			value="classpath&#42;:org/mydomain/myproject/&#42;&#42;/domain/model/&#42;.class" />
 *	</bean>
 * }
 * </pre>
 * 
 */
public class ScanningPersistenceUnitManager extends
		DefaultPersistenceUnitManager {

	private final Logger log = LoggerFactory
			.getLogger(ScanningPersistenceUnitManager.class);

	protected String classpathPatterns = null;

	public String getClasspathPatterns() {
		return classpathPatterns;
	}

	/**
	 * Classpath patterns to scan for entities.
	 * Pattern could use * or ** jokers
	 * You can specify more than one pattern by using , or ; separators
	 * 
	 * Following values are valid classpath patterns :
	 *  - classpath:org/mydomain/myproject/&#42;&#42;/domain/model/&#42;.class
	 *  - classpath&#42;:org/mydomain/myproject/&#42;&#42;/domain/model/&#42;.class
	 *  - classpath&#42;:org/mydomain/myproject1/&#42;&#42;/domain/model/&#42;.class, classpath&#42;:org/mydomain/myproject2/&#42;&#42;/domain/model/&#42;.class
	 */
	public void setClasspathPatterns(String classpathPatterns) {
		this.classpathPatterns = classpathPatterns;
	}

	@Override
	protected void postProcessPersistenceUnitInfo(MutablePersistenceUnitInfo pui) {

		if (this.classpathPatterns != null) {

			String[] basePackages = StringUtils.tokenizeToStringArray(this.classpathPatterns,
					ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);

			ResourcePatternResolver rpr = new PathMatchingResourcePatternResolver(Thread.currentThread().getContextClassLoader());
			TypeFilter entityTypeFilter = new AnnotationTypeFilter(Entity.class);
			TypeFilter mappedSuperclassTypeFilter = new AnnotationTypeFilter(MappedSuperclass.class);
			MetadataReaderFactory metadataReaderFactory = new SimpleMetadataReaderFactory();
			MetadataReader metadataReader = null;
			Resource[] resources = new Resource[0];
			
			for (String basePackage : basePackages) {

				try {
					resources = rpr.getResources(basePackage);

					for (Resource resource : resources) {
						
						metadataReader = metadataReaderFactory.getMetadataReader(resource);
						if (entityTypeFilter.match(metadataReader, metadataReaderFactory) || mappedSuperclassTypeFilter.match(metadataReader, metadataReaderFactory)) {
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
