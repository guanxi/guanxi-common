/**
 * 
 */
package org.guanxi.test;

import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.Random;

import org.junit.ComparisonFailure;

/**
 * @author matthew
 *
 */
public class TestUtils {
    public static final Random random = new Random(System.currentTimeMillis());
    /**
     * This is a list of all the printable characters that I can type on
     * my keyboard.
     */
    public static final char[] characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890`¬!\"£$%^&*()_+-=\t[]{};:'@#~\\|,.<>/? ".toCharArray();

    /**
     * This generates a random string from the character array
     * defined within this class.
     * 
     * @param length
     * @return
     */
    public static String randomString(int length) {
        StringBuilder result;
        
        result = new StringBuilder();
        for (int i = 0;i < length;i++) {
            result.append(characters[Math.abs(random.nextInt()) % characters.length]);
        }
        
        return result.toString();
    }
    
    /**
     * This checks that two map objects are equivalent by checking the keys and values.
     * 
     * @param <A>
     * @param <B>
     * @param message
     * @param expected
     * @param actual
     */
    public static <A, B> void assertMapEquals(String message, Map<A, B> expected, Map<A, B> actual) {
        if ( actual == null && expected == null ) {
            return;
        }
        else if ( actual == null || expected == null ) {
            throw new ComparisonFailure(message, 
                    expected == null ? "null" : expected.toString(), 
                    actual   == null ? "null" : actual.toString());
        }
        assertEquals(message, expected.size(), actual.size());
        for ( A key : expected.keySet() ) {
            assertEquals("Differing values for key '" + key.toString() + "'", expected.get(key), actual.get(key));
        }
    }
}
