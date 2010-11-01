package org.resthub.core.context.persistence;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This singleton allow to store and share scanned persistence entities names
 * from context that should be included and excluded. After context scanning, it
 * will contain all found persistence entities that match specified criteria and
 * are, actually included but not excluded.
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
	private Map<String, Set<String>> excludedEntitiesMap = new HashMap<String, Set<String>>();

	static synchronized PersistenceContext getInstance() {
		if (instance == null) {
			instance = new PersistenceContext();
		}
		return instance;
	}

	/** A private Constructor prevents any other class from instantiating. */
	private PersistenceContext() {
	}

	/**
	 * Retrieve the list of all entities name for the provided persistence unit
	 * name that are both included and not excluded. ie definitive list of
	 * entities to manage
	 * 
	 * @param persistenceUnitName
	 * @return null if the persistenceUnitName is unknown
	 */
	public Set<String> getEntities(String persistenceUnitName) {
		Set<String> currentIncludedEntities = includedEntitiesMap.get(persistenceUnitName);
		Set<String> currentExcludedEntities = excludedEntitiesMap.get(persistenceUnitName);
		
		if ((currentExcludedEntities == null)||(currentExcludedEntities.isEmpty())) {
			return currentIncludedEntities;
		}
		
		Set<String> entities = new HashSet<String>();
		
		for (String entity : currentIncludedEntities) {
			if(!currentExcludedEntities.contains(entity)) {
				entities.add(entity);
			}
		}
		
		return entities;
	}

	/**
	 * Retrieve the list of all included entities name for the provided
	 * persistence unit name
	 * 
	 * @param persistenceUnitName
	 * @return null if the persistenceUnitName is unknown
	 */
	public Set<String> getIncludedEntities(String persistenceUnitName) {
		return includedEntitiesMap.get(persistenceUnitName);
	}

	/**
	 * Retrieve the list of all excluded entities name for the provided
	 * persistence unit name
	 * 
	 * @param persistenceUnitName
	 * @return null if the persistenceUnitName is unknown
	 */
	public Set<String> getExcludedEntities(String persistenceUnitName) {
		return excludedEntitiesMap.get(persistenceUnitName);
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
	 * Exclude a list of entities name to the persistence context for the
	 * persistence unit name provided
	 * 
	 * @param persistenceUnitName
	 * @param entities
	 */
	public void addExcludedEntities(String persistenceUnitName,
			Set<String> entities) {
		excludedEntitiesMap.put(persistenceUnitName, entities);
	}

	/**
	 * Include all entities to the current entities name to the persistence
	 * context for the persistence unit name provided
	 * 
	 * @param persistenceUnitName
	 * @param entities
	 */
	public void includeAll(String persistenceUnitName, Set<String> entities) {
		Set<String> currentEntities = this.includedEntitiesMap
				.get(persistenceUnitName);
		if (currentEntities == null) {
			this.addIncludedEntities(persistenceUnitName, entities);
		} else {
			currentEntities.addAll(entities);
			this.includedEntitiesMap.put(persistenceUnitName, currentEntities);
		}
	}

	/**
	 * Exclude all entities to the current entities name to the persistence
	 * context for the persistence unit name provided
	 * 
	 * @param persistenceUnitName
	 * @param entities
	 */
	public void excludeAll(String persistenceUnitName, Set<String> entities) {
		Set<String> currentEntities = this.excludedEntitiesMap
				.get(persistenceUnitName);
		if (currentEntities == null) {
			this.addExcludedEntities(persistenceUnitName, entities);
		} else {
			currentEntities.addAll(entities);
			this.excludedEntitiesMap.put(persistenceUnitName, currentEntities);
		}
	}

	/**
	 * Flush context : Clear all entities lists for a given persistence unit
	 * name
	 * 
	 * @param persistenceUnitName
	 */
	public void clearPersistenceUnit(String persistenceUnitName) {
		this.includedEntitiesMap.remove(persistenceUnitName);
		this.excludedEntitiesMap.remove(persistenceUnitName);
	}

}
