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

package org.guanxi.common.trust.impl;

import org.guanxi.common.metadata.Metadata;
import org.guanxi.common.GuanxiException;
import org.guanxi.common.trust.TrustUtils;
import org.guanxi.xal.saml_2_0.metadata.EntityDescriptorType;
import org.guanxi.xal.saml_1_0.protocol.ResponseDocument;
import org.apache.xml.security.Init;
import org.apache.log4j.Logger;

import java.security.cert.X509Certificate;

/**
 * TrustEngine implementation that implements the rules of a Shibboleth federation such
 * as the UK Access Management Federation.
 *
 * @author alistair
 */
public class ShibbolethTrustEngineImpl extends SimpleTrustEngine {
  /** Our logger */
  private static final Logger logger = Logger.getLogger(ShibbolethTrustEngineImpl.class.getName());

  public ShibbolethTrustEngineImpl() {
    super();
    
    // Initialise the Apache security engine
    Init.init();
  }

  /** @see org.guanxi.common.trust.TrustEngine#trustEntity(org.guanxi.common.metadata.Metadata, Object)
   *  @link http://www.guanxi.uhi.ac.uk/index.php/Metadata_and_trust_in_the_UK_Access_Management_Federation
   * */
  public boolean trustEntity(Metadata entityMetadata, Object entityData) throws GuanxiException {
    // Handler private data is raw SAML2 metadata
    EntityDescriptorType saml2Metadata = (EntityDescriptorType)entityMetadata.getPrivateData();

    // PKIX validation based on certs in metadata
    if (entityData instanceof ResponseDocument) {
      // Entity data is the SAML Response from the IdP
      ResponseDocument samlResponse = (ResponseDocument)entityData;

      // First thing is check to see if the signature verifies
      if (!TrustUtils.verifySignature(samlResponse)) {
        logger.error("IdP signature failed validation");
        return false;
      }

      // Validation via embedded certificates
      if (TrustUtils.validateWithEmbeddedCert((ResponseDocument)entityData, saml2Metadata)) {
        return true;
      }

      // Validation via PKIX
      if (TrustUtils.validatePKIX((ResponseDocument)entityData, saml2Metadata, caCerts)) {
        return true;
      }
    }

    // PKIX validation based on certs from back channel connection
    if (entityData instanceof X509Certificate) {
      // Entity data is the X509 from the connection
      X509Certificate x509CertFromConnection = (X509Certificate)entityData;
      
      return TrustUtils.validatePKIXBC(x509CertFromConnection, saml2Metadata, caCerts);
    }


    return false;
  }
}
