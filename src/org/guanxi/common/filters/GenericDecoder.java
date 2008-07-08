/**
 * 
 */
package org.guanxi.common.filters;

import java.io.IOException;
import java.io.Writer;

/**
 * <p>This decoder implements a generic style of decoding which can support
 * RFC2253 style string decoding, as well as others of a similar style.</p>
 * http://www.ietf.org/rfc/rfc2253.txt
 * 
 * <p>The string to be decoded will have sequences which start with a control
 * character followed by two hex characters. This represents a single character
 * and the decoder will replace it as appropriate.</p>
 * 
 * <p>This will decode any string of that format. All {control}{hex}{hex} sequences 
 * are decoded irrespective of if they are required to be encoded initially.</p>
 * 
 * @author matthew
 *
 */
//default is not an accident. This class is not required outside this package.
class GenericDecoder extends ProperFilterWriter {

	/**
	 * This enumerates the states that the decoder can be in.
	 * 
	 * @author matthew
	 *
	 */
	protected enum DecoderStatus {
		/**
		 * This is the status when the decoder has yet to
		 * encounter an escaped sequence or has completed
		 * the decoding of any escaped sequence.
		 */
		NORMAL,
		/**
		 * This is the status when the decoder has encountered
		 * the initial control character which indicates the 
		 * start of an escaped sequence.
		 */
		CONTROL_CHARACTER_ENCOUNTERED,
		/**
		 * This is the status when the decoder has encountered
		 * the initial control character and the first hex value 
		 * of the escape sequence.
		 */
		BUFFER_FILLED,
	}
	
	/**
	 * This is the character that indicates that the next two
	 * characters are the hex representation of a single
	 * character.
	 */
	private final char controlCharacter;
	
	/**
	 * This indicates the current status of the buffer. If the
	 * buffer is flushed when this is in a non NORMAL state then
	 * the current character being decoded will not be written.
	 */
	private DecoderStatus status;
	/**
	 * This contains the first character of the hex character pair
	 * of the escaped character sequence.
	 */
	private char buffer;
	
	/**
	 * This creates a decoder that will write to the provided output stream.
	 * @param out
	 */
	public GenericDecoder(Writer out, char controlCharacter) {
		super(out);
		
		this.controlCharacter = controlCharacter;
		status = DecoderStatus.NORMAL;
	}

	/**
	 * 
	 * @see java.io.FilterWriter#write(int)
	 */
	public void write(int c) throws IOException {
		char character;
		
		character = (char)(c & 0xFFFF);
		if( status == DecoderStatus.NORMAL ) {
			if ( character == controlCharacter ) {
				status = DecoderStatus.CONTROL_CHARACTER_ENCOUNTERED;
			}
			else {
				super.write(c);
			}
		}
		else if ( status == DecoderStatus.CONTROL_CHARACTER_ENCOUNTERED ) {
			buffer = character;
			status = DecoderStatus.BUFFER_FILLED;
		}
		else { // status == DecoderStatus.BUFFER_FILLED
			// the StringBuilder is explicitly used here to prevent the inefficient 
			//	'' + buffer + character
			// and the plain incorrect
			// 	buffer + character
			super.write(Integer.parseInt( new StringBuilder().append(buffer).append(character).toString(), 16 ));
			status = DecoderStatus.NORMAL;
		}
	}
}
