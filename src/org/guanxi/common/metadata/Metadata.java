/**
 * 
 */
package org.guanxi.common.metadata;

import java.util.Comparator;

/**
 * This is the super interface that allows a single generic
 * class to accommodate both IdP and SP Metadata.
 * 
 * @author matthew
 *
 */
public interface Metadata {
  /**
   * This will return the entityID of the IdP or SP.
   * 
   * @return The string representation of the entityID.
   */
  public String getEntityID();
  
  /**
   * This MetadataComparator exists to allow Metadata to be used
   * efficiently in TreeSets and TreeMaps. This assumes that entityIDs
   * are completely unique.
   * 
   * @author matthew
   */
  public static class MetadataComparator<A extends Metadata> implements Comparator<A> {
    /**
     * This compares the two metatadata objects.
     * The result is based upon the comparison of the entityID
     * of the two metadata objects.
     * 
     * @param arg0  The first metadata object for comparison
     * @param arg1  The second metadata object for comparison
     * @returns int A value indicating if the first metadata object is before (<0), after(>0), or equal(0) to the second metadata object.
     */
    public int compare(A arg0, A arg1) {
      return arg0.getEntityID().compareTo(arg1.getEntityID());
    }
  }
}
