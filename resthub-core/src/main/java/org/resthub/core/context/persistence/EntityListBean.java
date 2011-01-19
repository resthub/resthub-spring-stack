package org.resthub.core.context.persistence;

import java.util.HashSet;
import java.util.Set;

public class EntityListBean {

	private Set<String> entities = new HashSet<String>();
	private String persistenceUnitName;

	public Set<String> getEntities() {
		return entities;
	}

	public void setEntities(Set<String> entities) {
		this.entities = entities;
	}

	public String getPersistenceUnitName() {
		return persistenceUnitName;
	}

	public void setPersistenceUnitName(String persistenceUnitName) {
		this.persistenceUnitName = persistenceUnitName;
	}
	
}
