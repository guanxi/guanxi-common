/**
 * 
 */
package org.guanxi.common.filters;

import java.io.Writer;

/**
 * This class declaration allows the GenericDecoder to be subclassed outside
 * of the org.guanxi.common.filters package.
 * 
 * @author matthew
 *
 */
public class GenericDecoderSubclass extends GenericDecoder {

	public GenericDecoderSubclass(Writer out, char controlCharacter) {
		super(out, controlCharacter);
	}

}
