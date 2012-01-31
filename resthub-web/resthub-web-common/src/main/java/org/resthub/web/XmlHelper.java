package org.resthub.web;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.resthub.web.exception.SerializationException;

public class XmlHelper {

    public static String serialize(Object o) {
        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(o.getClass());
            OutputStream baOutputStream = new ByteArrayOutputStream();
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(o, baOutputStream);
            return baOutputStream.toString();
        } catch (JAXBException e) {
            throw new SerializationException(e);
        }
    }

    public static <T> T deserialize(String content, Class<T> type) {
        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(type);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            ByteArrayInputStream input = new ByteArrayInputStream(content.getBytes());
            return type.cast(unmarshaller.unmarshal(input));
        } catch (JAXBException e) {
        	 throw new SerializationException(e);
        }
    }

   

}
