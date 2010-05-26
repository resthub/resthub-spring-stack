/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.resthub.web.jersey;

/**
 *
 * @author Guillaume Zurbach
 */
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;


@Provider
@Named("jaxbContextResolver")
public class JAXBContextResolver implements ContextResolver<JAXBContext> {

	private JAXBContext context;

	public JAXBContextResolver() throws JAXBException {
		context = new JSONJAXBContext(JSONConfiguration.mappedJettison().build());
	}

	private void changeContext(Class<?> objectType) throws JAXBException {
		context = new JSONJAXBContext(JSONConfiguration.natural().build(), objectType);
	}

	@Override
	public JAXBContext getContext(Class<?> objectType) {
		try {
			changeContext(objectType);
		} catch (JAXBException ex) {
			Logger.getLogger(JAXBContextResolver.class.getName()).log(Level.SEVERE, null, ex);
		}
		return context;
	}
}