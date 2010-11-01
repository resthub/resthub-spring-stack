package org.resthub.core.context.persistence;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This singleton allow to store and share scanned persistence entities names
 * from context. After context scanning, it will contain all found persistence
 * entities that match specified criteria.
 * 
 * This singleton can be used by post processor to manipulate found entities.
 * 
 * For safety reasons, it is only accessible from its own package
 * 
 * @author bmeurant <Baptiste Meurant>
 */
class PersistenceContext {

	private static PersistenceContext instance;

	private Map<String, Set<String>> entitiesMap = new HashMap<String, Set<String>>();

	static synchronized PersistenceContext getInstance() {
		if (instance == null) {
			instance = new PersistenceContext();
		}
		return instance;
	}

	/** A private Constructor prevents any other class from instantiating. */
	private PersistenceContext() {
	}

	protected Map<String, Set<String>> getEntitiesMap() {

		return entitiesMap;
	}

	/**
	 * @see Map#containsKey(Object)
	 */
	public boolean containsKey(String persistenceUnitName) {
		return entitiesMap.containsKey(persistenceUnitName);
	}

	/**
	 * @see Map#get(Object)
	 */
	public Set<String> get(String persistenceUnitName) {
		return entitiesMap.get(persistenceUnitName);
	}

	/**
	 * @see Map#put(Object, Object)
	 */
	public Set<String> put(String persistenceUnitName, Set<String> entities) {
		return entitiesMap.put(persistenceUnitName, entities);
	}
	
	/**
	 * Add all entities to the current entities list for the specified persistence unit name
	 * 
	 * @param persistenceUnitName
	 * @param entities
	 */
	public void addAll(String persistenceUnitName, Set<String> entities) {
		Set<String> beanDefinitions = this.entitiesMap.get(persistenceUnitName);
		if (beanDefinitions == null) {
			this.put(persistenceUnitName, entities);
		} else {
			beanDefinitions.addAll(entities);
			this.entitiesMap.put(persistenceUnitName, beanDefinitions);
		}
	}

	/**
	 * Flush context : Clear all entities list for a given persistence unit name
	 * 
	 * @param persistenceUnitName
	 */
	public void clearPersistenceUnit(String persistenceUnitName) {
		this.entitiesMap.remove(persistenceUnitName);
	}

}
