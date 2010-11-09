package org.resthub.identityManager.test;

import junit.framework.Assert;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.firefox.FirefoxDriver;
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
	
		@Before
	public void setUp() throws Exception {
		
		
		/*Resource fileserver_xml = Resource.newResource("file:/D:/Documents%20and%20Settings/A501570/workspace/Z-resthub/resthub-identity/resthub-identity-manager/src/main/webapp/WEB-INF/web.xml");
        if(fileserver_xml == null ){
        	throw new Exception("fileserever null");
        }
		XmlConfiguration configuration = new XmlConfiguration(fileserver_xml.getInputStream());
        
		Server server = (Server)configuration.configure();
		
		server.start();
        server.join();
		*/
			
			
			
			Server server = new Server(9797);
		
		 /*
		 ServletContextHandler authorization = new ServletContextHandler(
				ServletContextHandler.SESSIONS);
		authorization.setContextPath("/identity");			

		authorization.getInitParams().put("contextConfigLocation", "classpath*:resthubContext.xml classpath:AuthorizationContext.xml");
		authorization.addServlet(SpringServlet.class, "/*");
		authorization.addEventListener(new ContextLoaderListener());
 
		// Add a context for resource service
		ServletContextHandler resource = new ServletContextHandler(
				ServletContextHandler.SESSIONS);
		resource.setContextPath("/resourceServer");		
		
		resource.getInitParams().put("contextConfigLocation", "classpath*:resthubContext.xml classpath:ResourceContext.xml");
		FilterHolder filterDef = new FilterHolder(DelegatingFilterProxy.class);
		filterDef.setName("oauth2filter");
		resource.addFilter(filterDef, "/*", 1);
		
		ServletHolder servletDef = new ServletHolder(SpringServlet.class);
		servletDef.setInitParameter("com.sun.jersey.spi.container.ResourceFilters", 
				"com.sun.jersey.api.container.filter.RolesAllowedResourceFilterFactory");
		resource.addServlet(servletDef, "/*");
		resource.addEventListener(new ContextLoaderListener());

		// Starts the server.
		ContextHandlerCollection handlers = new ContextHandlerCollection();
        handlers.setHandlers(new Handler[] {authorization, resource});
        server.setHandler(handlers);
		server.start();
		
			 * 
			 * */
			ServletContextHandler identity = new ServletContextHandler(
					ServletContextHandler.SESSIONS);
			identity.setContextPath("/identity");			

			identity.getInitParams().put("contextConfigLocation", "classpath*:resthubContext.xml classpath:AuthorizationContext.xml");
			identity.addServlet(SpringServlet.class, "/*");
			identity.addEventListener(new ContextLoaderListener());

			
			
			ContextHandlerCollection handlers = new ContextHandlerCollection();
	        handlers.setHandlers(new Handler[] {identity});
			/*
			WebAppContext wac = new WebAppContext();
			wac.setWar("target/identity-manager.war");
			
				server.setHandler(wac);
			*/
	        server.setHandler(handlers);
			System.out.println("We are starting");
			server.start();
			System.out.println("We did start");

		
		driver= new FirefoxDriver();
		selenium= new WebDriverBackedSelenium(driver, "http://127.0.0.1:9797/");		
	}
	
	@Test
	public void testLoginandDisplayHome() throws Exception {
	
		// given the webservice identityManager
		// given an user with login "testLogin" and password "testLoginPassword"
		
	
		
		selenium.open("/#/");
		Thread.sleep(500);
		deleteAllUsers();

		String login = "testLogin";
		String password = "testLoginPassword";

		CreateNewUser(login, password);

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
	public void CreateNewUser(String login, String password, String firstName,
			String lastName) throws Exception {
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
	public void CreateNewUser(String Login, String Password) throws Exception {
		CreateNewUser(Login, Password, "NewUserFirstName", "NewUserLastName");
	}

	/*
	 * This is not a TEST This Method is a Helper function to a create New user
	 * trough the web Interface, based on a firstName and a lastName Login is
	 * randomly generated
	 */
	public void CreateNewUserWithName(String firstName, String lastName)
			throws Exception {
		CreateNewUser("Aii0ii" + Math.random(), "toto", firstName, lastName);
	}

	/*
	 * Test the creation of a New user based on first and last name
	 */
	@Test
	public void testCreateNewUser() throws Exception {
		// given the webservices identityManager
		selenium.open("/identity/#/");
		Thread.sleep(500);
		deleteAllUsers();
		// after the creation of the new User

		CreateNewUserWithName("John", "Rambo");
		// then information about the new user are display in a table
		Assert.assertEquals("John Rambo", selenium
				.getText("//div[@id='content']/table/tbody/tr/td[2]"));
	}

	@Test
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
	public void testDeletionUser() throws Exception {
		// given ONE user in the database
		selenium.open("/identity/#/");
		Thread.sleep(500);
		deleteAllUsers();
		CreateNewUser("User", "Todelete");

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
