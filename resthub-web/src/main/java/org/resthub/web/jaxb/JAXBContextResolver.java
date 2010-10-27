package org.resthub.web.jaxb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
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

	/**
	 * Class names knowned by this resolver.
	 */
	protected List<String> knownClassNames = new ArrayList<String>();

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
	 * Scans the classpath for known Xml Root elements.<br/>
	 * Initialize the JAXBContext resolver with found classes.<br/>
	 * <br/>
	 * 
	 * @param classpathPatterns
	 *            The specified classpath patterns.
	 */
	public void setClasspathPatterns(String classpathPatterns) throws Exception {
		this.classpathPatterns = classpathPatterns;
		// Only if we set patterns.
		if (this.classpathPatterns != null) {
			String[] basePackages = StringUtils.tokenizeToStringArray(
					this.classpathPatterns,
					ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);

			ResourcePatternResolver rpr = new PathMatchingResourcePatternResolver(
					Thread.currentThread().getContextClassLoader());
			// Only process classes annotated with XmlRootElement.
			TypeFilter annotationTypeFilter = new AnnotationTypeFilter(
					XmlRootElement.class);

			MetadataReaderFactory metadataReaderFactory = new SimpleMetadataReaderFactory();
			MetadataReader metadataReader = null;

			Resource[] resources = new Resource[0];
			// For each specified packages.
			for (String basePackage : basePackages) {
				int found = 0;
				try {
					// Gets all classes
					resources = rpr.getResources(basePackage);
					for (Resource resource : resources) {
						// Keeps only those annotated with XmlRootElement.
						metadataReader = metadataReaderFactory
								.getMetadataReader(resource);
						if (annotationTypeFilter.match(metadataReader,
								metadataReaderFactory)) {
							String className = metadataReader
									.getClassMetadata().getClassName();
							knownClassNames.add(className);
							found++;
							if (logger.isDebugEnabled()) {
								logger.info("Xml Root element " + className
										+ " found from package " + basePackage
										+ ".");
							}
						}
					}
				} catch (IOException e) {
					logger.warn(
							"Error during scanning Xml root elements : cannot scan "
									+ basePackage + ".", e);
				}
				if (logger.isInfoEnabled()) {
					logger.info(basePackage + " successfully scanned : "
							+ found + " Xml root elements found.");
				}
			}
		}
		// Gets classes from class names.
		Class<?>[] knownClasses = new Class<?>[knownClassNames.size()];
		int i = 0;
		for (String className : knownClassNames) {
			knownClasses[i++] = Thread.currentThread().getContextClassLoader()
					.loadClass(className);
		}
		// Creates the context.
		context = JAXBContext.newInstance(knownClasses);
		if (logger.isInfoEnabled()) {
			logger.info("Create JAX-B context with known Xml root elements : "
					+ StringUtils.collectionToDelimitedString(knownClassNames,
							" "));
		}
	} // setClasspathPatterns().

	// ----------------------------------------------------------------------------------------------------------------
	// ContextResolver inherited methods

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
	} // getContext().

} // class JAXBContextResolver.