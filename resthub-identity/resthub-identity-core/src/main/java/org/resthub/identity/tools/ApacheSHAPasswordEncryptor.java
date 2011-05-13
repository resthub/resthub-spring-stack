package org.resthub.identity.tools;

import java.security.NoSuchAlgorithmException;

import javax.inject.Named;

import org.jasypt.util.password.PasswordEncryptor;
import org.resthub.core.context.persistence.ScanningPersistenceUnitManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is based on native JDK encryption caabilities and allows you to 
 * encrypt a password doing a non salt SHA-1 encryption.
 *
 * @author "Nicolas Morel <nicolas.morel@atosorigin.com>"
 * @author "Sebastien Deleuze <sebastien.deleuze@atosorigin.com>"
 */
@Named("passwordEncryptor")
public class ApacheSHAPasswordEncryptor implements PasswordEncryptor {
	
	private static final Logger logger = LoggerFactory.getLogger(ApacheSHAPasswordEncryptor.class);

    /**
     * <p>
     * Creates a new instance of <tt>ApacheSHAPasswordEncryptor</tt>
     * </p>
     */
    public ApacheSHAPasswordEncryptor() {
        
    }

    /**
     * ${@inheritDoc}
     */
    @Override
    @SuppressWarnings("restriction")
    public String encryptPassword(final String password) {
    	
		String encryptedPassword = "";
		try {
			encryptedPassword = "{SHA}" + new sun.misc.BASE64Encoder().encode(java.security.MessageDigest.getInstance("SHA1").digest(password.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.toString());
		}
    	
        return encryptedPassword;
    }

    /**
     * ${@inheritDoc}
     */
    @Override
    public boolean checkPassword(final String plainPassword, final String encryptedPassword) {
        final boolean isValid = encryptPassword(plainPassword).equals(encryptedPassword);
        return isValid;
    }
}
