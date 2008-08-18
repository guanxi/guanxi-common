/* CVS Header
   $
   $
*/

package org.guanxi.common.metadata;

import org.guanxi.xal.metadata.SPDescriptor;

/**
 * This is a wrapper around the IdPDescriptor XMLBean
 * which is used to load and save the cached metadata.
 *
 * @author alistair
 */
public class SPTemplateMetadata implements SPMetadata {
  /**
   * This is the metadata that this class provides a wrapper around.
   * The format of this metadata is specifically designed to make
   * the wrapper methods as direct as possible.
   */
  private final SPDescriptor metadata;

  /**
   * This creates a new metadata wrapper class for the metadata
   * provided. Once created the object is immutable. Multiple
   * different objects can be created for a single IdPDescriptor.
   *
   * @param metadata metadata describing the SP
   */
  public SPTemplateMetadata(SPDescriptor metadata) {
    this.metadata = metadata;
  }

  /**
   * @see org.guanxi.common.metadata.SPMetadata#getAssertionConsumerServiceURL()
   */
  public String getAssertionConsumerServiceURL() {
    return metadata.getAssertionConsumerServiceURL();
  }

  /**
   * @see org.guanxi.common.metadata.SPMetadata#getEntityID()
   */
  public String getEntityID() {
    return metadata.getEntityID();
  }

  public byte[] getX509Certificate() {
    return metadata.getSigningCertificate();
  }
}
