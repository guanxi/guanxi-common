/* CVS Header
   $
   $
*/

package org.guanxi.common.metadata;

import org.guanxi.xal.saml_2_0.metadata.EntityDescriptorType;
import org.guanxi.xal.saml_2_0.metadata.EndpointType;

/**
 * This is an implementation of the IdPMetadata interface that supports wrapping
 * the EntityDescriptorType. The EntityDescriptorType works best with the
 * metadata available from the UK Federation, hence the name.
 *
 * @author alistair
 */
public class SPMetadataImpl implements SPMetadata {
  /**
   * This stores the loaded SP Metadata, and this class wraps around the data
   * that this contains.
   */
  private EntityDescriptorType spMetadata;

  /**
   * This creates a new wrapper class for the metadata provided.
   *
   * @param spMetadata top level EntityDescriptor node
   */
  public SPMetadataImpl(EntityDescriptorType spMetadata) {
    this.spMetadata = spMetadata;
  }

  /**
   * This will return the entityID of the SP
   *
   * @return The string representation of the SP entityID.
   */
  public String getEntityID() {
    return spMetadata.getEntityID();
  }

  /**
   * This will return the URL of the SP Assertion Consumer Service
   *
   * @return This returns the Assertion Consumer Service URL
   */
  public String getAssertionConsumerServiceURL() {
    for (EndpointType currentEndpoint : spMetadata.getSPSSODescriptorArray()[0].getAssertionConsumerServiceArray()) {
      if (currentEndpoint.getBinding().equals("urn:oasis:names:tc:SAML:1.0:bindings:SOAP-binding")) {
        return currentEndpoint.getLocation();
      }
    }

    // this is currently left here because this is the old code and is the best
    // guess when no URL with the correct binding has been found
    return spMetadata.getAttributeAuthorityDescriptorArray()[0].getAttributeServiceArray()[0].getLocation();
  }
}
