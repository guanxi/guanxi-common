/**
 * 
 */
package org.guanxi.common.metadata;

import org.guanxi.xal.saml_2_0.metadata.EndpointType;
import org.guanxi.xal.saml_2_0.metadata.EntityDescriptorType;
import org.guanxi.xal.saml_2_0.metadata.KeyDescriptorType;
import org.guanxi.xal.saml_2_0.metadata.KeyTypes;

/**
 * This is an implementation of the IdPMetadata interface that supports wrapping
 * the EntityDescriptorType. The EntityDescriptorType works best with the
 * metadata available from the UK Federation.
 * 
 * @author matthew
 */
public class IdPMetadata_XML_EntityDescriptorType implements IdPMetadata {
  /**
   * This stores the loaded IdP Metadata, and this class wraps around the data
   * that this contains.
   */
  private final EntityDescriptorType idpMetadata;

  /**
   * This creates a new wrapper class for the metadata provided.
   * 
   * @param idpMetadata
   */
  public IdPMetadata_XML_EntityDescriptorType(EntityDescriptorType idpMetadata) {
    this.idpMetadata = idpMetadata;
  }

  /**
   * This will return the entityID of the IdP.
   * 
   * @return The string representation of the IdP entityID.
   */
  public String getEntityID() {
    return idpMetadata.getEntityID();
  }

  /**
   * This will return the URL of the IdP Attribute Authority. This will attempt
   * to get the Attribute Authority URL that corresponds to the
   * urn:oasis:names:tc:SAML:1.0:bindings:SOAP-binding binding. If this binding
   * cannot be found then the first URL will be returned.
   * 
   * @return This returns the Attribute Authority URL which can be used for
   *         Attribute transfer with the IdP.
   */
  // This should be updated to return the AA URL associated with the
  // correct binding - urn:oasis:names:tc:SAML:1.0:bindings:SOAP-binding
  public String getAttributeAuthorityURL() {

    for (EndpointType currentEndpoint : idpMetadata.getAttributeAuthorityDescriptorArray()[0].getAttributeServiceArray()) {
      if (currentEndpoint.getBinding().equals("urn:oasis:names:tc:SAML:1.0:bindings:SOAP-binding")) {
        return currentEndpoint.getLocation();
      }
    }

    // this is currently left here because this is the old code and is the best
    // guess when no URL with the correct binding has been found
    return idpMetadata.getAttributeAuthorityDescriptorArray()[0].getAttributeServiceArray()[0].getLocation();
  }

  /**
   * This will return the binary data associated with the signing certificate of
   * the IdP. If this cannot find the signing certificate then the first
   * certificate in the metadata will be returned.
   * 
   * @return This returns the binary representation of the X509 Signing
   *         Certificate for the IdP.
   */
  // KeyInfoType keyInfo = entityDescriptor.getAttributeAuthorityDescriptorArray()[0].getKeyDescriptorArray()[0].getKeyInfo();
  // X509DataType x509Data = keyInfo.getX509DataArray()[0];
  // byte[] bytes = x509Data.getX509CertificateArray()[0];
  public byte[] getSigningCertificate() {
    try {
      for ( KeyDescriptorType currentKey : idpMetadata.getAttributeAuthorityDescriptorArray()[0].getKeyDescriptorArray() ) {
        if (currentKey.getUse() == KeyTypes.SIGNING) {
          return currentKey.getKeyInfo().getX509DataArray()[0].getX509CertificateArray()[0];
        }
      }
  
      // this is currently left here because this is the old code and is the best
      // guess when no signing key can be found
      return idpMetadata.getAttributeAuthorityDescriptorArray()[0].getKeyDescriptorArray()[0].getKeyInfo().getX509DataArray()[0].getX509CertificateArray()[0];
    }
    catch ( ArrayIndexOutOfBoundsException e ) {
      // thrown if the key does not exist as binary data - for example just the key name could be given
      return null;
    }
  }

  /**
   * This will return the server certificate for the AA URL if it can be reliably determined.
   * This will almost certainly return null even for secured AA URLs because the format of
   * the metadata in this respect is not well known by myself (matthew).
   * TODO: Verify and correct the collection of the AA certificate.
   * 
   * @return This returns the binary representation of the Server certificate for the AA URL.
   */
  public byte[] getAACertificate() {
    try {
    	for (KeyDescriptorType currentKey : idpMetadata.getAttributeAuthorityDescriptorArray()[0].getKeyDescriptorArray()) {
      	if (currentKey.getUse() == KeyTypes.ENCRYPTION) {
        	return currentKey.getKeyInfo().getX509DataArray()[0].getX509CertificateArray()[0];
      	}
    	}
    	return null;
    }
    catch ( ArrayIndexOutOfBoundsException e ) {
    	return null;
    }
  }
}
