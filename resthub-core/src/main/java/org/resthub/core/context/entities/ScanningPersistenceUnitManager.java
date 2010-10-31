package org.resthub.core.context.entities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
 * Allow to scan JPA Entities in all dependencies (JAR) of the application, in
 * order to allow modular JPA applications whith a model classes distributed in
 * various modules.
 * 
 * By default with JPA, this is not possible within the same persistence unit,
 * so we implemented this using Spring classpath scanning capabilities.
 * 
 * Sample Spring configuration :
 * 
 * <pre>
 * {@code 
 * <bean id="scanningPersistenceUnitManager"
 * 	class="org.resthub.core.domain.dao.jpa.ScanningPersistenceUnitManager">
 * 	<property name="defaultDataSource" ref="dataSource" />
 * 	<property name="classpathPatterns"
 * 		value="classpath&#42;:org/mydomain/myproject/&#42;&#42;/domain/model/&#42;.class" />
 * </bean>
 * }
 * </pre>
 * 
 * @author Baptiste Meurant
 * 
 */
public class ScanningPersistenceUnitManager extends
		DefaultPersistenceUnitManager {

	private final Logger log = LoggerFactory
			.getLogger(ScanningPersistenceUnitManager.class);

	protected String classpathPatterns = null;

	private List<TypeFilter> typeFilters = new ArrayList<TypeFilter>();

	public String getClasspathPatterns() {
		return classpathPatterns;
	}

	/**
	 * Classpath patterns to scan for entities (holding annotation
	 * {@link MappedSuperclass} or {@link Entity}. Pattern could use * or **
	 * jokers You can specify more than one pattern by using , or ; separators
	 * 
	 * Following values are valid classpath patterns : -
	 * classpath:org/mydomain/myproject/&#42;&#42;/domain/model/&#42;.class -
	 * classpath&#42;:org/mydomain/myproject/&#42;&#42;/domain/model/&#42;.class
	 * - classpath&#42;:org/mydomain/myproject1/&#42;&#42;/domain/model/&#42;.
	 * class,
	 * classpath&#42;:org/mydomain/myproject2/&#42;&#42;/domain/model/&#42
	 * ;.class
	 * 
	 * Be attentive that using this property, entities-scan based configuration
	 * is ignored
	 */
	public void setClasspathPatterns(String classpathPatterns) {
		log
				.warn("ClasspathPattern definition found : entities-scan base configuration is ignored !!");
		this.classpathPatterns = classpathPatterns;
	}

	/**
	 * {@InheritDoc}
	 */
	@Override
	protected void postProcessPersistenceUnitInfo(MutablePersistenceUnitInfo pui) {

		List<String> entities;

		if (this.classpathPatterns != null) {

			this.setFilters();
			entities = findMatchingEntities(pui);

		} else {

			entities = getMatchingEntitiesFromContext(pui, pui.getPersistenceUnitName());
			ResthubEntitiesContext.getInstance().clearPersistenceUnit(
					pui.getPersistenceUnitName());
		}
		
		addEntitiesToPersistenceUnit(pui, entities);

		super.postProcessPersistenceUnitInfo(pui);
	}

	private void addEntitiesToPersistenceUnit(MutablePersistenceUnitInfo pui,
			List<String> entities) {
		for (String entityName : entities) {

			if (!pui.getManagedClassNames().contains(entityName)) {
				pui.addManagedClassName(entityName);
			}
			if (log.isDebugEnabled()) {
				log.debug("Entity " + entityName + " found.");
			}
		}

		if (log.isInfoEnabled()) {
			log.info("persistenceUnit " + pui.getPersistenceUnitName()
					+ " successfully scanned : " + entities.size()
					+ " entities found.");
		}
	}

	private List<String> getMatchingEntitiesFromContext(
			MutablePersistenceUnitInfo pui, String persistenceUnitName) {
		
		List<String> entities = ResthubEntitiesContext.getInstance().get(
				persistenceUnitName);

		return entities;
	}

	protected List<String> findMatchingEntities(MutablePersistenceUnitInfo pui) {
		String[] basePackages = StringUtils.tokenizeToStringArray(
				this.classpathPatterns,
				ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);

		ResourcePatternResolver rpr = new PathMatchingResourcePatternResolver(
				Thread.currentThread().getContextClassLoader());

		Resource[] resources = new Resource[0];
		List<String> entities = new ArrayList<String>();
		String entityName;

		for (String basePackage : basePackages) {

			try {
				resources = rpr.getResources(basePackage);

				for (Resource resource : resources) {

					entityName = this.isResourceMatching(resource);
					if (entityName != null) {
						entities.add(entityName);
					}
				}

			} catch (IOException e) {
				log.warn("Error during scanning entities : cannot scan "
						+ basePackage + ".", e);
			}
			if (log.isInfoEnabled()) {
				log.info(basePackage + " successfully scanned : "
						+ resources.length + " entities found.");
			}

		}
		return entities;
	}

	protected String isResourceMatching(Resource resource) throws IOException {

		MetadataReaderFactory metadataReaderFactory = new SimpleMetadataReaderFactory();
		MetadataReader metadataReader = null;
		metadataReader = metadataReaderFactory.getMetadataReader(resource);

		for (TypeFilter filter : this.typeFilters) {
			if (filter.match(metadataReader, metadataReaderFactory)) {
				return metadataReader.getClassMetadata().getClassName();
			}
		}

		return null;
	}

	private void setFilters() {
		this.typeFilters.add(new AnnotationTypeFilter(Entity.class));
		this.typeFilters.add(new AnnotationTypeFilter(MappedSuperclass.class));
	}
}
