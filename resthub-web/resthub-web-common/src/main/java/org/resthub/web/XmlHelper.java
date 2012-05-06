package org.resthub.web;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.resthub.web.exception.SerializationException;

/**
 * Helper for XML serialization and deserialization
 */
public class XmlHelper {

    /**
     * Serialize and object to an XML String representation
     * @param o The object to serialize
     * @return The XML String representation
     */
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

    /**
     * Deserialize a XML string
     * @param content The XML String object representation
     * @param type The type of the deserialized object instance
     * @return The deserialized object instance
     */
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
