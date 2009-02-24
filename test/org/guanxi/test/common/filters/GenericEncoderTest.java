/**
 * 
 */
package org.guanxi.test.common.filters;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.guanxi.common.filters.GenericEncoderSubclass;
import org.guanxi.test.TestUtils;
import org.junit.Test;

/**
 * This tests the GenericEncoder class.
 * 
 * @author matthew
 *
 */
public class GenericEncoderTest {
	/**
	 * This tests that the input and output of the encoder
	 * match when none of the characters are marked as requiring
	 * escapes.
	 * 
	 * @throws IOException 
	 */
	@Test
	public void testNoEncoding() throws IOException {
		StringWriter writer;
		StringBuffer output;
		GenericEncoderSubclass encoder;
		String input;
		
		writer = new StringWriter();
		output = writer.getBuffer();
		encoder = new GenericEncoderSubclass(writer) {

			@Override
			protected String escape(char c) {
				throw new RuntimeException("escape(c) was called");
			}

			@Override
			protected boolean requiresEscaping(char c) {
				return false;
			}
			
		};
		
		for ( int i = 0;i < 100;i++ ) {
			input = TestUtils.randomString(100);
			
			encoder.write(input);
			assertEquals("GenericEncoder has altered the data inappropriately", input, output.toString());
			output.delete(0, output.length());
		}
	}
	
	/**
	 * This tests that the input and output of the encoder
	 * match when all of the characters are marked as requiring
	 * escapes but the escape function does not alter the input.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testNoEscaping() throws IOException {
		StringWriter writer;
		StringBuffer output;
		GenericEncoderSubclass encoder;
		String input;
		
		writer = new StringWriter();
		output = writer.getBuffer();
		encoder = new GenericEncoderSubclass(writer) {

			@Override
			protected String escape(char c) {
				return "" + c;
			}

			@Override
			protected boolean requiresEscaping(char c) {
				return true;
			}
			
		};
		
		for ( int i = 0;i < 100;i++ ) {
			input = TestUtils.randomString(100);
			
			encoder.write(input);
			assertEquals("GenericEncoder has altered the data inappropriately", input, output.toString());
			output.delete(0, output.length());
		}
	}
}
