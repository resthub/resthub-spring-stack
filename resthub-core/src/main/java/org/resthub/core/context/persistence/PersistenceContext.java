package org.resthub.core.context.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class PersistenceContext {

	private static PersistenceContext instance;

	private Map<String, List<String>> entitiesMap = new HashMap<String, List<String>>();

	static synchronized PersistenceContext getInstance() {
		if (instance == null) {
			instance = new PersistenceContext();
		}
		return instance;
	}

	/** A private Constructor prevents any other class from instantiating. */
	private PersistenceContext() {
	}

	protected Map<String, List<String>> getEntitiesMap() {
		return entitiesMap;
	}

	protected void setEntitiesMap(Map<String, List<String>> entitiesMap) {
		this.entitiesMap = entitiesMap;
	}

	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	
	public boolean containsKey(String persistenceUnitName) {
		return entitiesMap.containsKey(persistenceUnitName);
	}

	public List<String> get(String persistenceUnitName) {
		return entitiesMap.get(persistenceUnitName);
	}

	public List<String> put(String persistenceUnitName, List<String> entities) {
		return entitiesMap.put(persistenceUnitName, entities);
	}
	
	public void addAll(String persistenceUnitName, List<String> entities) {
		List<String> beanDefinitions = this.entitiesMap.get(persistenceUnitName);
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
