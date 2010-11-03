package org.resthub.core.context.jaxb;

import java.util.HashSet;
import java.util.Set;

/**
 * This singleton allow to store and share scanned xml elements names from
 * context that should be included and excluded. After context scanning, it will
 * contain all found xml elements that match specified criteria and are,
 * actually included but not excluded.
 * 
 * This singleton can be used by post processor to manipulate found xml elements
 * and bound them.
 * 
 * For safety reasons, it is only accessible from its own package
 * 
 * @author bmeurant <Baptiste Meurant>
 */
class JAXBElementsContext {

	private static JAXBElementsContext instance;

	private Set<String> includedElements = new HashSet<String>();
	private Set<String> excludedElements = new HashSet<String>();

	static synchronized JAXBElementsContext getInstance() {
		if (instance == null) {
			instance = new JAXBElementsContext();
		}
		return instance;
	}

	/** A private Constructor prevents any other class from instantiating. */
	private JAXBElementsContext() {
	}

	/**
	 * Retrieve the list of all entities name for the provided persistence unit
	 * name that are both included and not excluded. ie definitive list of
	 * entities to manage
	 * 
	 */
	public Set<String> getXmlElements() {

		if ((excludedElements == null)
				|| (excludedElements.isEmpty())) {
			return includedElements;
		}

		Set<String> elements = new HashSet<String>();

		for (String element : includedElements) {
			if (!excludedElements.contains(element)) {
				elements.add(element);
			}
		}

		return elements;
	}

	/**
	 * Retrieve the list of all included xml elemnts name 
	 * 
	 */
	public Set<String> getIncludedElements() {
		return includedElements;
	}

	/**
	 * Retrieve the list of all excluded xml elements
	 * 
	 */
	public Set<String> getExcludedElements() {
		return excludedElements;
	}

	/**
	 * Include a list of xml elements name to the XML binding context 
	 * 
	 * @param entities
	 */
	public void includeAll(Set<String> elements) {
		includedElements.addAll(elements);
	}

	/**
	 * Exclude a list of xml elements name to the XML binding context 
	 * 
	 * @param entities
	 */
	public void excludeAll(Set<String> elements) {
		excludedElements.addAll(elements);
	}

	/**
	 * Flush context : Clear all xml elemnts lists 
	 * 
	 */
	public void clear() {
		this.includedElements.clear();
		this.excludedElements.clear();
	}

}
