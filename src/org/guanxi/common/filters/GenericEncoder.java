/**
 * 
 */
package org.guanxi.common.filters;

import java.io.IOException;
import java.io.Writer;

/**
 * <p>This encoder can escape arbitrary characters in any way appropriate. This
 * encoder is only sensitive to the current character being encoded.</p>
 * 
 * <p>If the flush method is overridden then the position in the current write 
 * may be determined. However that is not recommended. Flush may be called 
 * before the entire stream has been processed, which would break any encoding
 * that depended on different behaviour.</p>
 * 
 * <p>In order to support using a GenericDecoder to decode the encoded string the
 * following format must be used for escape characters:</p>
 * 
 * {escaped character} = {control character}{hex character}{hex character}<br/>
 * {control character} = Any single character<br/>
 * {hex character} = 0-9,A-F,a-f<br/>
 * 
 * @author matthew
 *
 */
abstract class GenericEncoder extends ProperFilterWriter {
	
	/**
	 * This creates a new Generic Encoder which encodes an arbitrary set
	 * of characters.
	 * 
	 * @param out
	 * @param escapeCharacters
	 */
	public GenericEncoder(Writer out) {
		super(out);
	}
	
	/**
	 * This performs the character encoding.
	 * @param c
	 */
	public void write(int c) throws IOException {
		char character;
		
		character = (char)(c & 0xFFFF);
		if ( requiresEscaping(character) ) {
			out.write(escape(character));
		}
		else {
			out.write(c);
		}
	}

	/**
	 * This will test the provided character to determine if
	 * it should be escaped.
	 * @param c
	 */
	protected abstract boolean requiresEscaping(char c);
	
	/**
	 * This will escape the provided character.
	 * 
	 * @param c
	 */
	protected abstract String escape(char c);
}
