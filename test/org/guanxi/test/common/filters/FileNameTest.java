/**
 * 
 */
package org.guanxi.test.common.filters;

import static org.junit.Assert.assertEquals;

import org.guanxi.common.filters.FileName;
import org.guanxi.test.TestUtils;
import org.junit.Test;

/**
 * This tests the FileName class.
 * 
 * @author matthew
 *
 */
public class FileNameTest {

	/**
	 * This tests that the correct exception is thrown when an attempt
	 * to encode a null string is made.
	 */
	@Test(expected = NullPointerException.class)
	public void testNullEncode() {
		FileName.encode(null);
	}
	
	/**
	 * This tests that the correct exception is thrown when an attempt
	 * to decode a null string is made.
	 */
	@Test(expected = NullPointerException.class)
	public void testNullDecode() {
		FileName.decode(null);
	}
	
	/**
	 * This tests the ability of the class to encode and decode known strings.
	 */
	@Test
	public void testKnownEscapes() {
		String encoded;
		
		for ( String current : new String[]{
			"a/b/c",
			"a\\b\\c",
			"a:b:c",
			"a;b;c",
			new String(new char[]{ 0, 1, 2 }),
		} ) {
			encoded = FileName.encode(current);
			assertEquals("The output of FileName.decode is not the same as the input to FileName.encode", current, FileName.decode(encoded));
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
			encoded = FileName.encode(input);
			assertEquals("The output of FileName.decode is not the same as the input to FileName.encode", input, FileName.decode(encoded));
		}
	}
}
