/**
 * 
 */
package org.guanxi.common.filters;

import java.io.Writer;

/**
 * This is a subclass of the ProperFilterWriter which is designed to
 * get around the package protected access level of the ProperFilterWriter.
 * 
 * @author matthew
 *
 */
public class ProperFilterWriterSubclass extends ProperFilterWriter {

    public ProperFilterWriterSubclass(Writer out) {
        super(out);
    }

}
