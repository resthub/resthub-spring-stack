package org.resthub.web.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.inject.Named;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.resthub.core.domain.model.Resource;
import org.resthub.web.tal.PageTemplate;
import org.resthub.web.tal.PageTemplateImpl;

import com.sun.jersey.api.view.Viewable;
import com.sun.jersey.spi.template.ViewProcessor;


@Provider
@Named("defaultViewProcessor")
public class DefaultViewProcessor implements ViewProcessor<String> {
	@Override
	public String resolve(String name) {
		return name;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void writeTo(String templateReference, Viewable viewable,
			OutputStream out) throws IOException {

		// Commit the status and headers to the HttpServletResponse
		out.flush();
		PrintWriter pw = new PrintWriter(out);
		pw.println("<html>");
		pw.println("<body>");
		pw.flush();
		
		if(viewable.getModel() instanceof Resource) {
			Resource resource = (Resource) viewable.getModel();
			
			
			try {
				JAXBContext jaxbContext = JAXBContext.newInstance(Resource.class);
				Marshaller marshaller = jaxbContext.createMarshaller();
				marshaller.marshal(resource, out);
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
						Boolean.TRUE);
			} catch (Exception e) {
				pw.println("Error");
			}
		} else if(viewable.getModel() instanceof Resource[]) {
			Resource[] resources = (Resource[]) viewable.getModel();
			
			try {
				JAXBContext jaxbContext = JAXBContext.newInstance(resources.getClass());
				Marshaller marshaller = jaxbContext.createMarshaller();
				marshaller.marshal(new JAXBElement(new QName("resources"), Resource.class, resources), out);
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
						Boolean.TRUE);
			} catch (Exception e) {
				pw.println("Error");
			}
		}
		

		pw.println("</body>");
		pw.println("</html>");
		pw.flush();

	}

}
