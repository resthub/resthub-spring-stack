package org.resthub.core.context.persistence;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This singleton allow to store and share scanned persistence entities names
 * from context that should be included and excluded. After context scanning, it
 * will contain all found persistence entities that match specified criteria.
 * 
 * This singleton can be used by post processor to manipulate found entities.
 * 
 * For safety reasons, it is only accessible from its own package
 * 
 * @author bmeurant <Baptiste Meurant>
 */
class PersistenceContext {

	private static PersistenceContext instance;

	private Map<String, Set<String>> includedEntitiesMap = new HashMap<String, Set<String>>();

	static synchronized PersistenceContext getInstance() {
		if (instance == null) {
			instance = new PersistenceContext();
		}
		return instance;
	}

	/** A private Constructor prevents any other class from instantiating. */
	private PersistenceContext() {
	}

	protected Map<String, Set<String>> getIncludedEntitiesMap() {

		return includedEntitiesMap;
	}

	/**
	 * Retrieve the list of all included entities name for the provided persistence unit name
	 * 
	 * @param persistenceUnitName
	 * @return null if the persistenceUnitName is unknown
	 */
	public Set<String> getIncludedEntities(String persistenceUnitName) {
		return includedEntitiesMap.get(persistenceUnitName);
	}

	/**
	 * Include a list of entities name to the persistence context for the
	 * persistence unit name provided
	 * 
	 * @param persistenceUnitName
	 * @param entities
	 */
	public void addIncludedEntities(String persistenceUnitName,
			Set<String> entities) {
		includedEntitiesMap.put(persistenceUnitName, entities);
	}

	/**
	 * Include all entities to the current entities name to the persistence context for the
	 * persistence unit name provided
	 * 
	 * @param persistenceUnitName
	 * @param entities
	 */
	public void includeAll(String persistenceUnitName, Set<String> entities) {
		Set<String> beanDefinitions = this.includedEntitiesMap
				.get(persistenceUnitName);
		if (beanDefinitions == null) {
			this.addIncludedEntities(persistenceUnitName, entities);
		} else {
			beanDefinitions.addAll(entities);
			this.includedEntitiesMap.put(persistenceUnitName, beanDefinitions);
		}
	}

	/**
	 * Flush context : Clear all entities lists for a given persistence unit name
	 * 
	 * @param persistenceUnitName
	 */
	public void clearPersistenceUnit(String persistenceUnitName) {
		this.includedEntitiesMap.remove(persistenceUnitName);
	}

}
