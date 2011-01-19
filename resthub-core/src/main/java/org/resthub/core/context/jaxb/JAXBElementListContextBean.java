package org.resthub.core.context.jaxb;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;

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
@Named("JAXBElementListContext")
class JAXBElementListContextBean {

	@Autowired(required = false)
	private List<JAXBElementListIncluderBean> includerBeans;

	@Autowired(required = false)
	private List<JAXBElementListExcluderBean> excluderBeans;

	private Set<String> includedElements;
	private Set<String> excludedElements;

	/**
	 * Retrieve the list of all entities name for the provided persistence unit
	 * name that are both included and not excluded. ie definitive list of
	 * entities to manage
	 * 
	 */
	public Set<String> getXmlElements() {

		if ((null == includedElements) && (null == excludedElements)) {
			includedElements = new HashSet<String>();
			excludedElements = new HashSet<String>();
			initElementsLists();
		}

		return getEffectiveXmlElements();
	}

	private Set<String> getEffectiveXmlElements() {
		if ((excludedElements == null) || (excludedElements.isEmpty())) {
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

	private void initElementsLists() {

		if (includerBeans != null) {
			for (JAXBElementListIncluderBean includerBean : includerBeans) {
				includedElements.addAll(includerBean.getElements());
			}
		}

		if (excluderBeans != null) {
			for (JAXBElementListExcluderBean excluderBean : excluderBeans) {
				excludedElements.addAll(excluderBean.getElements());
			}
		}

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

		if (null != includedElements) {
			this.includedElements.clear();
		}
		
		if (null != excludedElements) {
			this.excludedElements.clear();
		}
	}

}
