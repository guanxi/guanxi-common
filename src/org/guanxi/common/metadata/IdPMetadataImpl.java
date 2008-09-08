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
  private byte[] signingCertificate;
  /**
   * This is the X509 Certificate used to identify the server for the AA URL.
   */
  private byte[] aaCertificate;
  
  public IdPMetadataImpl() {}
  
  public IdPMetadataImpl(String entityID, String attributeAuthorityURL, byte[] signingCertificate, byte[] aaCertificate) {
    setEntityID(entityID);
    setAttributeAuthorityURL(attributeAuthorityURL);
    setSigningCertificate(signingCertificate);
    setAACertificate(aaCertificate);
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
  public byte[] getSigningCertificate() {
    return signingCertificate;
  }

  /**
   * @param certificate the x509Certificate to set
   */
  public void setSigningCertificate(byte[] certificate) {
    signingCertificate = certificate;
  }

  /**
   * @return the aaCertificate
   */
  public byte[] getAACertificate() {
    return aaCertificate;
  }

  /**
   * @param aaCertificate the aaCertificate to set
   */
  public void setAACertificate(byte[] aaCertificate) {
    this.aaCertificate = aaCertificate;
  }

}
