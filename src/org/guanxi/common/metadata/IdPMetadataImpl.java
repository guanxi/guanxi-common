/**
 * 
 */
package org.guanxi.common.metadata;

/**
 * This is a plain implementation of IdPMetadata with getter and setter methods.
 * This does not back onto any XML Beans or Hibernate etc. Basically this is as
 * simple as it gets.
 * 
 * @author matthew
 */
public class IdPMetadataImpl implements IdPMetadata {
  /**
   * This is the Attribute Authority URL for the IdP.
   */
  private String attributeAuthorityURL;
  /**
   * This is the entityID of the IdP.
   */
  private String entityID;
  /**
   * This is the X509 Certificate used to sign SAML assertions from the IdP.
   */
  private byte[] x509Certificate;
  
  public IdPMetadataImpl() {}
  
  public IdPMetadataImpl(String entityID, String attributeAuthorityURL, byte[] x509Certificate) {
    setEntityID(entityID);
    setAttributeAuthorityURL(attributeAuthorityURL);
    setX509Certificate(x509Certificate);
  }

  /**
   * @return the attributeAuthorityURL
   */
  public String getAttributeAuthorityURL() {
    return attributeAuthorityURL;
  }

  /**
   * @param attributeAuthorityURL the attributeAuthorityURL to set
   */
  public void setAttributeAuthorityURL(String attributeAuthorityURL) {
    this.attributeAuthorityURL = attributeAuthorityURL;
  }

  /**
   * @return the entityID
   */
  public String getEntityID() {
    return entityID;
  }

  /**
   * @param entityID the entityID to set
   */
  public void setEntityID(String entityID) {
    this.entityID = entityID;
  }

  /**
   * @return the x509Certificate
   */
  public byte[] getX509Certificate() {
    return x509Certificate;
  }

  /**
   * @param certificate the x509Certificate to set
   */
  public void setX509Certificate(byte[] certificate) {
    x509Certificate = certificate;
  }

}
