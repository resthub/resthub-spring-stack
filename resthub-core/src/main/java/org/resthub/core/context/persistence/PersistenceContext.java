package org.resthub.core.context.persistence;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	
	public boolean containsKey(String persistenceUnitName) {
		return entitiesMap.containsKey(persistenceUnitName);
	}

	public Set<String> get(String persistenceUnitName) {
		return entitiesMap.get(persistenceUnitName);
	}

	public Set<String> put(String persistenceUnitName, Set<String> entities) {
		return entitiesMap.put(persistenceUnitName, entities);
	}
	
	public void addAll(String persistenceUnitName, Set<String> entities) {
		Set<String> beanDefinitions = this.entitiesMap.get(persistenceUnitName);
		if (beanDefinitions == null) {
			this.put(persistenceUnitName, entities);
		}
		else {
			beanDefinitions.addAll(entities);
			this.entitiesMap.put(persistenceUnitName, beanDefinitions);
		}
	}
	
	public void clearPersistenceUnit(String persistenceUnitName) {
		this.entitiesMap.remove(persistenceUnitName);
	}
	

}
