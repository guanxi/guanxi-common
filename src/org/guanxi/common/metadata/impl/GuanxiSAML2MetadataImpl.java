//: "The contents of this file are subject to the Mozilla Public License
//: Version 1.1 (the "License"); you may not use this file except in
//: compliance with the License. You may obtain a copy of the License at
//: http://www.mozilla.org/MPL/
//:
//: Software distributed under the License is distributed on an "AS IS"
//: basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
//: License for the specific language governing rights and limitations
//: under the License.
//:
//: The Original Code is Guanxi (http://www.guanxi.uhi.ac.uk).
//:
//: The Initial Developer of the Original Code is Alistair Young alistair@codebrane.com
//: All Rights Reserved.
//:

package org.guanxi.common.metadata.impl;

import org.guanxi.common.metadata.IdPMetadata;
import org.guanxi.common.metadata.SPMetadata;
import org.guanxi.common.definitions.Shibboleth;
import org.guanxi.xal.saml_2_0.metadata.EntityDescriptorType;
import org.guanxi.xal.saml_2_0.metadata.EndpointType;

public class GuanxiSAML2MetadataImpl implements IdPMetadata, SPMetadata {
  /** The SAML2 metadata backing this object */
  private EntityDescriptorType saml2Metadata = null;

  /** @see org.guanxi.common.metadata.Metadata#getEntityID()  */
  public String getEntityID() {
    return saml2Metadata.getEntityID();
  }

  /** @see org.guanxi.common.metadata.IdPMetadata#getAttributeAuthorityURL() */
  public String getAttributeAuthorityURL() {
    for (EndpointType currentEndpoint : saml2Metadata.getAttributeAuthorityDescriptorArray()[0].getAttributeServiceArray()) {
      if (currentEndpoint.getBinding().equals(Shibboleth.BROWSER_POST_BINDING)) {
        return currentEndpoint.getLocation();
      }
    }

    // this is currently left here because this is the old code and is the best
    // guess when no URL with the correct binding has been found
    return saml2Metadata.getAttributeAuthorityDescriptorArray()[0].getAttributeServiceArray()[0].getLocation();
  }

  /** @see org.guanxi.common.metadata.SPMetadata#getAssertionConsumerServiceURL() */
  public String getAssertionConsumerServiceURL() {
    for (EndpointType currentEndpoint : saml2Metadata.getSPSSODescriptorArray()[0].getAssertionConsumerServiceArray()) {
      if (currentEndpoint.getBinding().equals(Shibboleth.BROWSER_POST_BINDING)) {
        return currentEndpoint.getLocation();
      }
    }

    // this is currently left here because this is the old code and is the best
    // guess when no URL with the correct binding has been found
    return saml2Metadata.getAttributeAuthorityDescriptorArray()[0].getAttributeServiceArray()[0].getLocation();
  }

  /** @see org.guanxi.common.metadata.IdPMetadata#setPrivateData(Object)  */
  public void setPrivateData(Object privateData) {
    this.saml2Metadata = (EntityDescriptorType)privateData;
  }

  /** @see org.guanxi.common.metadata.IdPMetadata#getPrivateData()  */
  public Object getPrivateData() {
    return saml2Metadata;
  }
}
