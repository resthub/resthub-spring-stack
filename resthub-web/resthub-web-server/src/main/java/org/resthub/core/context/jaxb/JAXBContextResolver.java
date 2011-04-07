package org.resthub.core.context.jaxb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlRootElement;

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
import org.springframework.util.StringUtils;

/**
 * This resolver is used by Jersey to (un)marshall classes annotated with
 * XMLRootElement annotation.<br/>
 * <br/>
 * 
 * Particulary relevant for managing generic collections.<br/>
 * <br/>
 * 
 * <b>Must be declared as a Spring bean when used with Spring.</b>
 * 
 * @author feugy <Damien Feugas>
 * @author bmeurant <Baptiste Meurant>
 */
@Provider
@Consumes( { MediaType.APPLICATION_XML, "text/xml" })
@Produces( { MediaType.APPLICATION_XML, "text/xml" })
public class JAXBContextResolver implements ContextResolver<JAXBContext> {

	// ----------------------------------------------------------------------------------------------------------------
	// Protected attributes

	/**
	 * Class logger
	 */
	protected static Logger logger = LoggerFactory
			.getLogger(JAXBContextResolver.class);

	private JAXBElementListContextBean jaxbElementListContextBean;

	public void setJaxbElementListContextBean(
			JAXBElementListContextBean jaxbElementListContextBean) {
		this.jaxbElementListContextBean = jaxbElementListContextBean;
	}

	/**
	 * Class names knowned by this resolver.
	 */
	protected List<String> knownClassNames = new ArrayList<String>();

	/**
	 * Filters that the class should match to be added to JAXB context manager
	 */
	private List<TypeFilter> typeFilters = new ArrayList<TypeFilter>();

	/**
	 * JAX-B context used for marshalling and unmarshalling.
	 */
	protected JAXBContext context;

	// ----------------------------------------------------------------------------------------------------------------
	// Properties

	/**
	 * Stors the classpath patterns used for scanning XML root elements.
	 */
	protected String classpathPatterns = null;

	public String getClasspathPatterns() {
		return classpathPatterns;
	}

	public void init() {
		Set<String> elements;

		elements = getMatchingElementsFromContext();
		addElementsToJAXBContext(elements);

		jaxbElementListContextBean.clear();
	}

	protected Set<String> getMatchingElementsFromContext() {

		Set<String> elements = jaxbElementListContextBean.getXmlElements();

		return elements;
	}

	/**
	 * Classpath patterns to scan for XML root elements.<br/>
	 * Pattern could use * or ** jokers.<br/>
	 * You can specify more than one pattern by using , or ; separators.<br/>
	 * <br/>
	 * 
	 * Following values are valid classpath patterns :
	 * <ul>
	 * <li>classpath:org/mydomain/myproject/&#42;&#42;/domain/model/&#42;.class</li>
	 * <li>classpath&#42;:org/mydomain/myproject/&#42;&#42;/domain/model/&#42;.
	 * class</li>
	 * <li>classpath&#42;:org/mydomain/myproject1/&#42;&#42;/domain/model/&#42;.
	 * class</li>
	 * <li>classpath&#42;:org/mydomain/myproject2/&#42;&#42;/domain/model/&#42
	 * ;.class</li>
	 * </ul>
	 * 
	 * Be attentive that using this property, entities-scan based configuration
	 * is ignored
	 * 
	 * @param classpathPatterns
	 *            The specified classpath patterns.
	 */
	public void setClasspathPatterns(String classpathPatterns) {
		logger
				.warn("ClasspathPattern definition found : jaxb-elements-scan base configuration is ignored !!");
		this.classpathPatterns = classpathPatterns;
		if (this.classpathPatterns != null) {

			this.setFilters();
			Set<String> elements;

			elements = findMatchingElements();
			jaxbElementListContextBean.clear();

			addElementsToJAXBContext(elements);
		}
	}

	private String[] extractBasePackages() {
		String[] basePackages = StringUtils.tokenizeToStringArray(
				this.classpathPatterns,
				ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
		return basePackages;
	}

	protected Set<String> findMatchingElements() {
		String[] basePackages = extractBasePackages();

		Set<String> elements = new HashSet<String>();

		for (String basePackage : basePackages) {

			elements.addAll(findMatchingElementsFromPackage(basePackage));

		}
		return elements;
	}

	protected Set<String> findMatchingElementsFromPackage(String basePackage) {
		ResourcePatternResolver rpr = new PathMatchingResourcePatternResolver(
				Thread.currentThread().getContextClassLoader());

		String elementName;
		Resource[] resources = new Resource[0];
		Set<String> elements = new HashSet<String>();

		try {
			resources = rpr.getResources(basePackage);

			for (Resource resource : resources) {

				elementName = this.isResourceMatching(resource);
				if (elementName != null) {
					elements.add(elementName);
				}
			}

		} catch (IOException e) {
			logger.warn("Error during scanning elements : cannot scan "
					+ basePackage + ".", e);
		}
		if (logger.isInfoEnabled()) {
			logger.info(basePackage + " successfully scanned : "
					+ resources.length + " elements found.");
		}
		return elements;
	}

	protected String isResourceMatching(Resource resource) throws IOException {

		MetadataReaderFactory metadataReaderFactory = new SimpleMetadataReaderFactory();
		MetadataReader metadataReader = metadataReaderFactory
				.getMetadataReader(resource);

		for (TypeFilter filter : this.typeFilters) {
			if (filter.match(metadataReader, metadataReaderFactory)) {
				return metadataReader.getClassMetadata().getClassName();
			}
		}

		return null;
	}

	/**
	 * Gives access to the JAX-B context.
	 * 
	 * @param type
	 *            Class of the (un)marshalled object.
	 * @return the relevant JAX-B context, or null if this context is not
	 *         applicable.
	 */
	@Override
	public JAXBContext getContext(Class<?> type) {
		// Checks if the requested type is one of those known.
		return knownClassNames.contains(type.getName()) ? context : null;
	}

	protected void setFilters() {
		// Only process classes annotated with XmlRootElement.
		this.typeFilters.add(new AnnotationTypeFilter(XmlRootElement.class));
	}

	private void addElementsToJAXBContext(Set<String> elements) {
		try {
			// Gets classes from class names.
			Class<?>[] knownClasses = new Class<?>[elements.size()];
			int i = 0;
			for (String className : elements) {
				knownClasses[i++] = Thread.currentThread()
						.getContextClassLoader().loadClass(className);
				knownClassNames.add(className);
			}
			// Creates the context.
			context = JAXBContext.newInstance(knownClasses);
			if (logger.isInfoEnabled()) {
				logger
						.info("Create JAX-B context with known Xml root elements : "
								+ StringUtils.collectionToDelimitedString(
										elements, " "));
			}
		} catch (ClassNotFoundException classEx) {
			 logger.error("Exception when adding class to the JAXBContext", classEx);

		} catch (JAXBException jaxbEx) {
			logger.error("Exception when adding class to the JAXBContext", jaxbEx);
		}

	}

}