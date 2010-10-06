package org.resthub.web.test.jaxb;

import java.io.IOException;
import static org.junit.Assert.assertFalse;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests XML and JSON marshalling.
 * @author Guillaume Zurbach
 */
public abstract class AbstractJaxbTest<T> {

	T entity;

	@Before
	public void setUp() {
		entity = createTestResource();
	}

	/**
	 * Override this method in order to creates an instance for tests.
	 * @return T
	 * @throws Exception
	 */
	protected abstract T createTestResource();

	@Test
	public void testXMLMarshalling() throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(entity.getClass());
		OutputStream baOutputStream = new ByteArrayOutputStream();
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.marshal(entity, baOutputStream);
		System.out.println(baOutputStream.toString());
		assertFalse("Unable to produce XML from class '" + entity.getClass() + "'.", baOutputStream.toString().isEmpty());
	}

	@Test
	public void testJSONMarshalling() throws JAXBException, JsonMappingException, JsonGenerationException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		OutputStream baOutputStream = new ByteArrayOutputStream();
   		AnnotationIntrospector introspector = new JaxbAnnotationIntrospector();
   		mapper.getDeserializationConfig().setAnnotationIntrospector(introspector);
   		mapper.getSerializationConfig().setAnnotationIntrospector(introspector);
   		mapper.writeValue( baOutputStream, entity );
		System.out.println(baOutputStream.toString());
		assertFalse("Unable to produce JSON from class '" + entity.getClass() + "'.", baOutputStream.toString().isEmpty());
	}
}