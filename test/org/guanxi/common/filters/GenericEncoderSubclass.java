/**
 * 
 */
package org.guanxi.common.filters;

import java.io.Writer;

/**
 * This class declaration allows the GenericEncoder to be subclassed outside
 * of the org.guanxi.common.filters package.
 * 
 * @author matthew
 *
 */
public abstract class GenericEncoderSubclass extends GenericEncoder {

	public GenericEncoderSubclass(Writer out) {
		super(out);
	}

}
