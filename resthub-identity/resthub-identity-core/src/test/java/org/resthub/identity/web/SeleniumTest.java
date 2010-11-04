package org.resthub.identity.web;

import com.thoughtworks.selenium.SeleneseTestCase;

/* This class is not used yet */
/* The fact is taht we need in the setUp to lauch the webApp that we need */
/* The easier way seems to lauch through jetty */
public class SeleniumTest extends SeleneseTestCase {

	public void setUp() throws Exception {
		setUp("http://127.0.0.1:8080/identity/", "*chrome");
	}

	public void testLoginandDisplayHome() throws Exception {
		// given the webservice identityManager
		// given an user with login "testLogin" and password "testLoginPassword"
		selenium.open("/identity/#/");
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
		assertEquals("Bonjour " + login, selenium
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
	public void testCreateNewUser() throws Exception {
		// given the webservices identityManager
		selenium.open("/identity/#/");
		Thread.sleep(500);
		deleteAllUsers();
		// after the creation of the new User

		CreateNewUserWithName("John", "Rambo");
		// then information about the new user are display in a table
		verifyEquals("John Rambo", selenium
				.getText("//div[@id='content']/table/tbody/tr/td[2]"));
	}

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
		assertFalse(selenium
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
		verifyFalse(selenium.isTextPresent("User Todelete"));

	}
}
