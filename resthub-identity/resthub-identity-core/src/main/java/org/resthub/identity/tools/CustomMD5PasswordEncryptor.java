package org.resthub.identity.tools;

import org.jasypt.digest.StandardStringDigester;
import org.jasypt.util.password.PasswordEncryptor;

/**
 * This class is based on Jasypt and allows you to encrypt a password doing
 * a 1k times MD5 and a random salt of 8 bytes by default.
 *
 * @author "Nicolas Morel <nicolas.morel@atosorigin.com>"
 */
public class CustomMD5PasswordEncryptor implements PasswordEncryptor {
    // The internal digester used
    private final StandardStringDigester digester;

    /**
     * <p>
     * Creates a new instance of <tt>CustomMD5PasswordEncryptor</tt>
     * and sets the size of the salt to be used to compute the digest.
     * This mechanism is explained in
     * <a href="http://www.rsasecurity.com/rsalabs/node.asp?id=2127"
     * target="_blank">PKCS &#035;5: Password-Based Cryptography Standard</a>.
     * </p>
     *
     * <p>
     * If salt size is set to zero, then no salt will be used.
     * </p>
     *
     * @param saltSizeBytes the size of the salt to be used, in bytes.
     */
    public CustomMD5PasswordEncryptor(int saltSizeBytes) {
        super();
        this.digester = new StandardStringDigester();
        this.digester.setSaltSizeBytes(saltSizeBytes);
        this.digester.initialize();
    }

    /**
     * ${@inheritDoc}
     */
    @Override
    public String encryptPassword(final String password) {
        return this.digester.digest(password);
    }

    /**
     * ${@inheritDoc}
     */
    @Override
    public boolean checkPassword(final String plainPassword, final String encryptedPassword) {
        return this.digester.matches(plainPassword, encryptedPassword);
    }
}
