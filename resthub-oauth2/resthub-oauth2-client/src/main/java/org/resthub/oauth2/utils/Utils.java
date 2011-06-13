package org.resthub.oauth2.utils;

import java.util.Date;
import java.util.Random;

/**
 * Various tools
 */
public class Utils {

    /**
     * Creates a random.
     */
    private static Random random = new Random(new Date().getTime());

    /**
     * Géenerate a random string, containing character (either upper and lower
     * case) and figures.
     * 
     * @param n
     *            String desired length.
     * @return The generated string.
     */
    public static String generateString(int n) {
        StringBuilder pwd = new StringBuilder();
        int c = 'A';
        for (int i = 0; i < n; i++) {
            // Un randon sur la catégorie.
            switch (random.nextInt(3)) {
            case 0:
                // On insère une lettre.
                c = '0' + random.nextInt(10);
                break;
            case 1:
                // On insère une minuscule.
                c = 'a' + random.nextInt(26);
                break;
            case 2:
                // On insère une majuscule.
                c = 'A' + random.nextInt(26);
                break;
            }
            pwd.append((char) c);
        }
        // Renvoi le mot de passe.
        return pwd.toString();
    } // generateString().
}
