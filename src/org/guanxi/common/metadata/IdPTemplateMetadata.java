/**
 * 
 */
package org.guanxi.common.metadata;

import org.guanxi.xal.metadata.IdPDescriptor;

/**
 * This is a wrapper around the IdPDescriptor XMLBean
 * which is used to load and save the cached metadata.
 * 
 * @author matthew
 */
public class IdPTemplateMetadata implements IdPMetadata {
  
  /**
   * This is the metadata that this class provides a wrapper around.
   * The format of this metadata is specifically designed to make
   * the wrapper methods as direct as possible.
   */
  private final IdPDescriptor metadata;

  /**
   * This creates a new metadata wrapper class for the metadata
   * provided. Once created the object is immutable. Multiple
   * different objects can be created for a single IdPDescriptor.
   * 
   * @param metadata
   */
  public IdPTemplateMetadata(IdPDescriptor metadata) {
    this.metadata = metadata;
  }
  
  /**
   * @see org.guanxi.common.metadata.IdPMetadata#getAttributeAuthorityURL()
   */
  public String getAttributeAuthorityURL() {
    return metadata.getAttributeAuthorityURL();
  }

  /**
   * @see org.guanxi.common.metadata.IdPMetadata#getEntityID()
   */
  public String getEntityID() {
    return metadata.getEntityID();
  }

  /**
   * @see org.guanxi.common.metadata.IdPMetadata#getX509Certificate()
   */
  public byte[] getX509Certificate() {
    return metadata.getSigningCertificate();
  }

}
