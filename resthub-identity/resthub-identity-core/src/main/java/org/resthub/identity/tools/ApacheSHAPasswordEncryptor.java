package org.resthub.identity.tools;

import javax.inject.Named;
import org.jasypt.digest.StandardStringDigester;
import org.jasypt.util.password.PasswordEncryptor;

/**
 * This class is based on Jasypt and allows you to encrypt a password doing
 * a 1k times SHA-1 and a random salt of 8 bytes by default.
 *
 * @author "Nicolas Morel <nicolas.morel@atosorigin.com>"
 */
@Named("passwordEncryptor")
public class ApacheSHAPasswordEncryptor implements PasswordEncryptor {

    /**
     * The internal digester used
     */
    private final StandardStringDigester digester;

    /**
     * <p>
     * Creates a new instance of <tt>ApacheSHAPasswordEncryptor</tt>
     * </p>
     */
    public ApacheSHAPasswordEncryptor() {
        super();

        this.digester = new StandardStringDigester();
        this.digester.setAlgorithm("SHA-1");
        this.digester.setIterations(1);
        this.digester.setSaltSizeBytes(8);
        this.digester.setPrefix("{SHA}");
        this.digester.setInvertPositionOfSaltInMessageBeforeDigesting(true);
        this.digester.setInvertPositionOfPlainSaltInEncryptionResults(true);
        this.digester.setUseLenientSaltSizeCheck(true);

        // Initialize the password encryptor with a 32-bit salt (4 bytes)
        this.digester.setSaltSizeBytes(4);
    }

    /**
     * ${@inheritDoc}
     */
    @Override
    public String encryptPassword(final String password) {
        final String encryptedPassword = this.digester.digest(password);
        return encryptedPassword;
    }

    /**
     * ${@inheritDoc}
     */
    @Override
    public boolean checkPassword(final String plainPassword, final String encryptedPassword) {
        final boolean isValid = this.digester.matches(plainPassword, encryptedPassword);
        return isValid;
    }
}
