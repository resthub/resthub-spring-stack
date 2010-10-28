package org.resthub.identity.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.resthub.core.test.service.AbstractResourceServiceTest;
import org.resthub.identity.model.User;

public class UserServiceTest extends AbstractResourceServiceTest<User, UserService> {

	@Inject
	@Named("userService")
	@Override
	public void setResourceService(UserService resourceService) {
		super.setResourceService(resourceService);
	}
	
	@Override
    @Test(/*expected = UnsupportedOperationException.class*/)
    public void testUpdate() throws Exception {
		
		User u=new User();
		String firstName="Maxence";
		String firstNameAbr="Max";
		u.setFirstName(firstName);

		String lastName="Dalmais";
		u.setLastName(lastName);
		
		u=this.resourceService.create(u);
		u.setFirstName(firstNameAbr);
		u=resourceService.update(u);
		
		assertEquals(u.getFirstName(), firstNameAbr);
        //throw new UnsupportedOperationException("Not supported yet.");
    }
	
	 @Test
	    public void shouldGetUserByAuthenticationInformationWhenOK(){
	    	String login="alexOK";
	    	String password="alex-pass";
	    	User u = new User();
	    	u.setLogin(login);
	    	u.setPassword(password);
	    	resourceService.create(u);
	    	
	    	u =resourceService.authenticateUser(login, password);
	    	assertNotNull(u);
	    	assertEquals(u.getLogin(), login);
	    	assertEquals(u.getPassword(), password);
	    }
	    
	   @Test
	    public void shouldGetNullWhenBadPassword(){
	    	String login="alexBadPassword";
	    	String password="alex-pass";
	    	String badPassword="alex-bad-pass";

	    	User u = new User();
	    	u.setLogin(login);
	    	u.setPassword(password);
	    	resourceService.create(u);
	    	
	    	u =resourceService.authenticateUser(login, badPassword);
	    	assertNull(u); 	 
	    }
	   
	    @Test
	    public void shouldGetNullWhenBadLogin(){
	    	String login="alexBadLogin";
	    	String badLogin="alex";
	    	String password="alex-password";
	    	
	    	User u = new User();
	    	u.setLogin(login);
	    	u.setPassword(password);
	    	resourceService.create(u);
	    	
	    	u =resourceService.authenticateUser(badLogin, password);
	    	assertNull(u); 	 
	    }
}
