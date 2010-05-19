package org.resthub.identity.domain.model;

import static org.junit.Assert.assertFalse;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.Before;
import org.junit.Test;
import org.resthub.identity.model.Group;
import org.resthub.identity.model.User;



public class TestGroupJaxb {
	
	private Group group;
	private User user1;
	private User user2;

    @Before
    public void setUp() {
    	group = new Group("GroupRef");
    	user1 = new User("User1Login");
    	user2 = new User("User2Login");
    	
    	group.setName("GroupName");
    	group.addPermission("perm1");
    	group.addPermission("perm2");
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
}
