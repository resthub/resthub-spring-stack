package org.resthub.identity.tools;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author "Nicolas Morel <nicolas.morel@atosorigin.com>"
 */
public class ApacheSHAPasswordEncryptorTest {

    // Basic encryptor to make tests
    private ApacheSHAPasswordEncryptor encryptor = new ApacheSHAPasswordEncryptor();

    @Test
    public void shouldEncryptPassword() {
        // Given a password
        final String password = "testPassword";

        // When encrypting the password
        String encryptedPassword = encryptor.encryptPassword(password);

        // Then we should have an encrypted password
        assertNotNull("The encrypted password shouldn't be null.", encryptedPassword);
        assertFalse("The encrypted password shouldn't be empty.", encryptedPassword.isEmpty());
    }

    @Test
    public void shouldBeAbleToCheckPassword() {
        // Given an encrypted password
        final String password = "testPassword";
        final String encryptedPassword = encryptor.encryptPassword(password);

        // When we check the password
        boolean isPasswordValid = encryptor.checkPassword(password, encryptedPassword);

        // Then the result should be true
        assertTrue("The password should be valid.", isPasswordValid);
    }

    @Test
    public void shouldBeAbleToAcceptAnApacheEncryptedPassword() {
        // Given a htpasswd generated password (htpasswd -nbs test t3st)
        final String password = "t3st";
        final String encryptedPassword = "{SHA}u3/Rg4+2cdohm4CmQtP9Qq45HX0=";

        // When we check the password
        boolean isPasswordValid = encryptor.checkPassword(password, encryptedPassword);

        // Then the result should be true
        assertTrue("The password should be valid.", isPasswordValid);
    }
}
