package org.resthub.identity.model;

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

public class GroupMarshallingTest {
	
	private Group group;
	private User user1;
	private User user2;

    @Before
    public void setUp() {
    	group = new Group();
    	user1 = new User();
    	user2 = new User();

    	group.setName("GroupName");
    	group.addPermission("perm1");
    	group.addPermission("perm2");

		user1.setLogin("User1Login");
		user2.setLogin("User2Login");
		
		group.addUser(user1);
    	group.addUser(user2);
    	user1.addGroup(group);
    }

    @Test
    public void testGroupMarshalling() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Group.class);
        OutputStream baOutputStream = new ByteArrayOutputStream();
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(group, baOutputStream);
        System.out.println(baOutputStream.toString());
        assertFalse(baOutputStream.toString().isEmpty());
    }
	
	@Test
	public void testGroupJSONMarshalling() throws JAXBException, JsonMappingException, JsonGenerationException, IOException {
		ObjectMapper mapper = new ObjectMapper();
    	OutputStream baOutputStream = new ByteArrayOutputStream();
   	    AnnotationIntrospector introspector = new JaxbAnnotationIntrospector();
   	    mapper.getDeserializationConfig().setAnnotationIntrospector(introspector);
   	    mapper.getSerializationConfig().setAnnotationIntrospector(introspector);
   	    mapper.writeValue( baOutputStream, group );
        System.out.println(baOutputStream.toString());
        assertFalse(baOutputStream.toString().isEmpty());
	}
}
