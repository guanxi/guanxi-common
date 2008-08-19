/**
 * 
 */
package org.guanxi.common.filters;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import java.util.TreeMap;

/**
 * <p>This class features an encoder and a decoder that implements RFC2253 style 
 * string handling<p>
 * http://www.ietf.org/rfc/rfc2253.txt
 * 
 * <p>The rules can be summarised as follows:
 * <ul>
 * 	<li>The following characters must be encoded:
 * 	 <ul>
 * 	  <li>A space or # character at the start of the string</li>
 *    <li>A space at the end of the string</li>
 *    <li>One of the characters: ',' '+' '"' '\' '<' '>' ';' ' '</li>
 * 	 </ul>
 *  </li>
 *  <li>The encoding is done by converting the character into
 *  	a two digit hex code. This is then preceded by a backslash (\).</li>
 * </ul></p>
 * 
 * <p>To simplify the encoding, all spaces and # characters will be encoded.
 * It is possible to supply a custom set of characters to encode.</p>
 * 
 * <p>This will decode any string of that format. All \HH sequences are decoded
 * irrespective of if they are required to be encoded initially.</p>
 * 
 * <p>This is not appropriate for encoding strings that will be used as file names
 * because the \ character cannot be used in file names on Windows machines.</p>
 * 
 * @author matthew
 *
 */
public class RFC2253 {
	
	/**
	 * This is a cached version of the RFC2253 compliant
	 * encoder.
	 */
	private static Encoder encoder;
	/**
	 * This is the destination of the encoder and will
	 * contain the encoded string.
	 */
	private static StringWriter encoderBuffer;
	
	/**
	 * This is a cached version of the RFC2253 compliant
	 * decoder.
	 */
	private static GenericDecoder decoder;
	/**
	 * This is the destination of the decoder and will
	 * contain the decoded string.
	 */
	private static StringWriter decoderBuffer;
	
	/**
	 * This initialises the cached encoder and decoder.
	 */
	static {
		encoderBuffer = new StringWriter();
		encoder = new Encoder(encoderBuffer);
		
		decoderBuffer = new StringWriter();
		decoder = new GenericDecoder(decoderBuffer, '\\');
	}
	
	/**
	 * This will encode the String provided in an RFC2253 compliant way.
	 * 
	 * @param string
	 * @return
	 */
	public static String encode(String string) {
		encoderBuffer.getBuffer().setLength(0);
		
		try {
			encoder.write(string);
		}
		catch (IOException e) {
			throw new RuntimeException(e); 
			// Its better to throw than eat.
			// The exception becomes unchecked because encoding should never error.
		}
		return encoderBuffer.toString();
	}
	
	/**
	 * This will encode the escaped characters in the String provided in
	 * an RFC2253 style. Note that the produced String will not be RFC2253
	 * compliant unless it includes the characters: ',' '+' '"' '\' '<' '>' ';' ' '
	 * 
	 * @param string
	 * @param escapeCharacters
	 * @return
	 */
	public static String encode(String string, char[] escapeCharacters) {
		StringWriter encoderBuffer;
		Encoder encoder;
		
		encoderBuffer = new StringWriter();
		encoder = new Encoder(encoderBuffer, escapeCharacters);
		
		try {
			encoder.write(string);
		}
		catch (IOException e) {
			throw new RuntimeException(e); 
			// Its better to throw than eat.
			// The exception becomes unchecked because encoding should never error.
		}
		return encoderBuffer.toString();
	}
	
	/**
	 * This will decode a String encoded in an RFC2253 style.
	 *  
	 * @param string
	 * @return
	 */
	public static String decode(String string) {
		decoderBuffer.getBuffer().setLength(0);
		
		try {
			decoder.write(string);
		}
		catch (IOException e) {
			throw new RuntimeException(e); 
			// Its better to throw than eat.
			// Decoding can fail however it cannot (really) fail with an IOException.
			// Decoding can throw NumberFormatExceptions if the String is badly formed.
		}
		return decoderBuffer.toString();
	}
	
	/**
	 * <p>This encoder implements RFC2253 style string encoding.</p>
	 * http://www.ietf.org/rfc/rfc2253.txt
	 * 
	 * <p>The rules can be summarised as follows:
	 * <ul>
	 * 	<li>The following characters must be encoded:
	 * 	 <ul>
	 * 	  <li>A space or # character at the start of the string</li>
	 *    <li>A space at the end of the string</li>
	 *    <li>One of the characters: , + " \ < > ;
	 * 	 </ul>
	 *  </li>
	 *  <li>The encoding is done by converting the character into
	 *  	a two digit hex code. This is then preceded by a backslash (\).</li>
	 * </ul></p>
	 * 
	 * <p>In order to simplify the implementation of this all spaces and # characters
	 * will be encoded.</p>
	 * 
	 * @author matthew
	 */
	private static class Encoder extends GenericEncoder {
		
		/**
		 * This contains all of the characters that must be escaped.
		 * The value of this map is the escaped form, in a format that
		 * can be written directly to the OutputStream.
		 */
		private Map<Character, String> escapeCharacters;
		
		/**
		 * This creates a new RFC2253 Encoder encoding only the characters
		 * explicitly required by RFC2253.
		 * 
		 * @param out
		 */
		public Encoder(Writer out) {
			this(out, new char[] { ',', '+', '"', '\\', '<', '>', ';', ' ', '\t', '#' });
		}
		
		/**
		 * This creates a new RFC2253 Encoder which encodes an arbitrary set
		 * of characters. If the characters required by RFC2253 are not included
		 * in this set then this will not produce RFC2253 compliant strings.
		 * 
		 * @param out
		 * @param escapeCharacters
		 */
		public Encoder(Writer out, char[] escapeCharacters) {
			super(out);
			
			this.escapeCharacters = new TreeMap<Character, String>();
			
			for ( char character : escapeCharacters ) {
				this.escapeCharacters.put(character, String.format("\\%02X", (int)character) );
			}
			this.escapeCharacters.put('\\', String.format("\\%02X", (int)'\\'));
			// the character used to escape the sequence must always be escaped if it
			// is present in any input string.
		}

		/**
		 * This will escape the provided character.
		 * 
		 * @param c
		 */
		protected String escape(char c) {
			if ( !escapeCharacters.containsKey(c) ) {
				return String.format("\\%02X", c);
			}
			
			return '\\' + escapeCharacters.get(c);
		}

		/**
		 * This will test the provided character to determine if
		 * it should be escaped.
		 * @param c
		 */
		protected boolean requiresEscaping(char c) {
			return escapeCharacters.containsKey(c);
		}
	}
}
