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
	
	/**
	 * This test that it is possible to replace every character
	 * with the same escaped character.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testUniformEscaping() throws IOException {
		StringWriter writer;
		StringBuffer output;
		GenericEncoderSubclass encoder;
		StringBuffer comparisonGeneration;
		String comparison;
		
		writer = new StringWriter();
		output = writer.getBuffer();
		encoder = new GenericEncoderSubclass(writer) {

			@Override
			protected String escape(char c) {
				return "a";
			}

			@Override
			protected boolean requiresEscaping(char c) {
				return true;
			}
			
		};
		
		comparisonGeneration = new StringBuffer();
		for ( int i = 0;i < 100;i++ ) {
			comparisonGeneration.append("a");
		}
		comparison = comparisonGeneration.toString();
		
		for ( int i = 0;i < 100;i++ ) {
			encoder.write(TestUtils.randomString(100));
			assertEquals("GenericEncoder has altered the data inappropriately", comparison, output.toString());
			output.delete(0, output.length());
		}
	}
	
	/**
	 * This tests the ability of the GenericEncoder to escape specific
	 * characters and not others.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testSelectiveEscaping() throws IOException {
		StringWriter writer;
		StringBuffer output;
		GenericEncoderSubclass encoder;
		String input;
		final char[][] encoding;
		
		// this stores the mapping between unescaped and escaped characters
		// this is stored in a format which makes it easy for the input string
		// to be manipulated outside of the encoder. This leads to a suboptimal
		// encoder implementation.
		encoding = new char[][]{
				new char[]{ '1', 'a' },
				new char[]{ '2', 'b' },
				new char[]{ '3', 'c' },
				new char[]{ '4', 'd' },
				new char[]{ '5', 'e' },
				new char[]{ '6', 'f' },
				new char[]{ '7', 'g' },
				new char[]{ '8', 'h' },
				new char[]{ '9', 'i' },
				new char[]{ '0', 'j' },
		};
		
		writer = new StringWriter();
		output = writer.getBuffer();
		encoder = new GenericEncoderSubclass(writer) {

			@Override
			protected String escape(char c) {
				for ( int i = 0;i < encoding.length;i++ ) {
					if ( encoding[i][0] == c ) {
						return encoding[i][1] + "";
					}
				}
				return c + "";
			}

			@Override
			protected boolean requiresEscaping(char c) {
				return Character.isDigit(c);
			}
			
		};
		
		for ( int i = 0;i < 100;i++ ) {
			input = TestUtils.randomString(100);
			
			encoder.write(input);
			for ( int j = 0;j < encoding.length;j++ ) {
				input.replace(encoding[i][0], encoding[i][1]);
			}
			
			assertEquals("GenericEncoder has altered the data inappropriately", input, output.toString());
			output.delete(0, output.length());
		}
	}
}
