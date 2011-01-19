package org.resthub.identityManager.test;

import junit.framework.Assert;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.context.ContextLoaderListener;

import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.thoughtworks.selenium.Selenium;



/* This class is not used yet */
/* The fact is taht we need in the setUp to lauch the webApp that we need */
/* The easier way seems to lauch through jetty */
/* to use it, we need to change the pom.xml to remove the excludes of this file for testing*/
public class SeleniumTest {

	 WebDriver driver ;
     Selenium selenium;
	
	Server server;
		
	@Before
	public void setUp() throws Exception {
		
		
			server = new Server(8080);

			ServletContextHandler ser = new ServletContextHandler(
					ServletContextHandler.SESSIONS);
			
			ser.addFilter(OpenEntityManagerInViewFilter.class, "/*", 1);
					
			ser.setContextPath("/identity");			
			ser.getInitParams().put("contextConfigLocation", "classpath*:resthubContext.xml classpath*:applicationContext.xml classpath:AuthorizationContext.xml");
						
			ser.addServlet(SpringServlet.class, "/api/*");
			ser.addServlet(DefaultServlet.class, "/*");
			
			ser.addEventListener(new ContextLoaderListener());
			ser.setResourceBase("./target/identity-manager");
				
			ContextHandlerCollection handlers = new ContextHandlerCollection();
	        handlers.setHandlers(new Handler[] {ser});
	        server.setHandler(handlers);
			server.start();
			
		driver= new HtmlUnitDriver();
		((HtmlUnitDriver) driver).setJavascriptEnabled(true);
		
		selenium= new WebDriverBackedSelenium(driver, "http://127.0.0.1:8080/");		
	}
	
	@After
	public void tearDown() throws Exception {
		server.stop();
	}
		
	@Test
	public void testLoginandDisplayHome() throws Exception {

	
		// given the webservice identityManager
		// given an user with login "testLogin" and password "testLoginPassword"
			
		selenium.open("/identity/#");
		Thread.sleep(500);
		selenium.click("link=users");
		Thread.sleep(5000);
		deleteAllUsers();
	
		String login = "testLogin";
		String password = "testLoginPassword";

		createNewUser(login, password);

		selenium.click("link=User Login");
		Thread.sleep(1000);

		// when the user log
		selenium.type("//input[@name='Login']", login);
		selenium.type("Password", password);
		selenium.click("LoginButton");
		Thread.sleep(3000);

		// then is display 'Bonjour X', with X being X est le login
		Assert.assertEquals("Bonjour " + login, selenium
				.getText("//div[@id='content']/h1"));
	}

	/*
	 * This is not a TEST This Method is a Helper function to a create New user
	 * trough the web Interface
	 */
	public void createNewUser(String login, String password, String firstName,
			String lastName) throws Exception {
		
		selenium.open("/identity/#");
		selenium.click("link=user");
		Thread.sleep(6000);
		selenium.type("firstName", firstName);
		selenium.type("lastName", lastName);
		selenium.type("login", login);
		selenium.type("password", password);
		selenium.type("passwordCheck", password);
		selenium.type("email", "toto@atosworldline.com");

		// Then we proceed to the creation
		selenium.click("user-proceed");
		int miliSec = 0;
		while (!selenium.isElementPresent("//div[@id='content']/table/tbody")) {
			Thread.sleep(100);
			miliSec += 100;
			if (miliSec > 3000) {
				break;
			}
		}

	}

	/*
	 * This is not a TEST This Method is a Helper function to a create New user
	 * trough the web Interface, based on a Login and a password
	 */
	public void createNewUser(String Login, String Password) throws Exception {
		createNewUser(Login, Password, "NewUserFirstName", "NewUserLastName");
	}

	/*
	 * This is not a TEST This Method is a Helper function to a create New user
	 * trough the web Interface, based on a firstName and a lastName Login is
	 * randomly generated
	 */
	public void createNewUserWithName(String firstName, String lastName)
			throws Exception {
		createNewUser("Aii0ii" + Math.random(), "toto", firstName, lastName);
	}

	/*
	 * Test the creation of a New user based on first and last name
	 */
	@Test
	@Ignore
	public void testCreateNewUser() throws Exception {
		// given the webservices identityManager
		selenium.open("/identity/#/");
		Thread.sleep(500);
		deleteAllUsers();
		// after the creation of the new User

		createNewUserWithName("John", "Rambo");
		// then information about the new user are display in a table
		Assert.assertEquals("John Rambo", selenium
				.getText("//div[@id='content']/table/tbody/tr/td[2]"));
	}

	@Test
	@Ignore
	public void testCreateNewUserFailedWhenPAsswordAreNotEgals()
			throws Exception {
		// given the webservices identityManager
		// given a new user
		selenium.open("/identity/#/");
		selenium.click("link=user");
		Thread.sleep(10000);
		selenium.type("firstName", "John");
		selenium.type("lastName", "Rambo");
		selenium.type("login", "Aii0ii" + Math.random());
		selenium.type("password", "toto");
		selenium.type("passwordCheck", "totoOOO");
		selenium.type("email", "toto");

		// when we proceed to the creation with 2 differents password
		selenium.click("user-proceed");
		int miliSec = 0;

		while (!selenium.isElementPresent("//div[@id='content']/table/tbody")) {
			Thread.sleep(100);
			miliSec += 100;
			if (miliSec > 3000) {
				break;
			}
		}
		// the table listing the user isn't display
		Assert.assertFalse(selenium
				.isElementPresent("//div[@id='content']/table/tbody"));
	}

	/*
	 * This is not a TEST This method is an helper method.
	 *  It deletes the first user listed on the web interface
	 */
	public boolean deleteFirstUser() throws Exception {
		selenium.click("link=users");
		Thread.sleep(500);
		if (selenium.isElementPresent("checkbox-0")) {
			selenium.click("checkbox-0");
			selenium.click("delete");
			Thread.sleep(1000);
			selenium.getConfirmation();
			return true;
		} else {
			return false;
		}

	}
	
	/*
	 * This is not a TEST This method is an helper method.
	 *  It deletes all the users
	 */
	public void deleteAllUsers() throws Exception {
		while (deleteFirstUser()) {
		}
	}
	/*
	 * This is TEST
	 * It tests the deletion of a user
	 */
	@Test
	@Ignore
	public void testDeletionUser() throws Exception {
		// given ONE user in the database
		selenium.open("/identity/#/");
		Thread.sleep(500);
		deleteAllUsers();
		createNewUser("User", "Todelete");

		// When we delete it
		selenium.click("link=users");
		Thread.sleep(1000);
		selenium.click("checkbox-0");
		selenium.click("delete");
		Thread.sleep(1000);
		selenium.getConfirmation();
		// We can't find his name in the list
		Assert.assertFalse(selenium.isTextPresent("User Todelete"));

	}

	
	public void testUpdate() throws Exception {
		// TODO Auto-generated method stub
		
	}
}
