/**
 * 
 */
package org.guanxi.test.common.filters;

import static org.junit.Assert.assertEquals;

import org.guanxi.common.filters.RFC2253;
import org.guanxi.test.TestUtils;
import org.junit.Test;

/**
 * This tests the RFC2253 class.
 * 
 * @author matthew
 *
 */
public class RFC2253Test {

	/**
	 * This tests that the correct exception is thrown when an attempt
	 * to encode a null string is made.
	 */
	@Test(expected = NullPointerException.class)
	public void testNullEncode() {
		RFC2253.encode(null);
	}
	
	/**
	 * This tests that the correct exception is thrown when an attempt
	 * to decode a null string is made.
	 */
	@Test(expected = NullPointerException.class)
	public void testNullDecode() {
		RFC2253.decode(null);
	}
	
	/**
	 * This tests the ability of the class to encode and decode known strings.
	 */
	@Test
	public void testKnownEscapes() {
		String encoded;

		for ( String current : new String[]{
			"a,b,c",
			"a+b+c",
			"a\"b\"c",
			"a\\b\\c",
			"a<b<c",
			"a>b>c",
			"a;b;c",
			"a b c",
			"a\tb\tc",
			"a#b#c",
			new String(new char[]{ 0, 1, 2 }),
		} ) {
			System.out.println(current);
			encoded = RFC2253.encode(current);
			System.out.println(encoded);
			assertEquals("The output of RFC2253.decode is not the same as the input to RFC2253.encode", current, RFC2253.decode(encoded));
		}
	}
	
	/**
	 * This tests the ability of the class to encode and decode random strings.
	 */
	@Test
	public void testRandomEscapes() {
		String input, encoded;
		
		for ( int i = 0;i < 100;i++ ) {
			input   = TestUtils.randomString(100);
			encoded = RFC2253.encode(input);
			assertEquals("The output of RFC2253.decode is not the same as the input to RFC2253.encode", input, RFC2253.decode(encoded));
		}
	}
}
