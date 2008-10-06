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
import org.guanxi.xal.saml_2_0.metadata.EntityDescriptorType;
import org.guanxi.xal.saml_2_0.metadata.IDPSSODescriptorType;
import org.guanxi.xal.saml_2_0.metadata.KeyDescriptorType;
import org.guanxi.xal.w3.xmldsig.X509DataType;
import org.guanxi.xal.w3.xmldsig.SignatureType;
import org.guanxi.xal.w3.xmldsig.KeyInfoType;
import org.guanxi.xal.saml_1_0.protocol.ResponseDocument;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.Init;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import java.security.cert.X509Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateException;
import java.io.ByteArrayInputStream;
import java.io.IOException;

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
    EntityDescriptorType saml = (EntityDescriptorType)entityMetadata.getPrivateData();

    // entity data is the SAML Response from the IdP
    ResponseDocument samlResponse = (ResponseDocument)entityData;

    // First thing is check to see if the signature verifies
    if (!verifySignature(samlResponse)) {
      logger.error("IdP signature failed validation");
      return false;
    }

    // Load up the IdP's metadata
    if (saml.getIDPSSODescriptorArray() != null) {
      IDPSSODescriptorType[] idpInfos = saml.getIDPSSODescriptorArray();

      /* Direct signature validation via X509/X509Certificate
       * In this case we compare the X509 in the metadata with the X509
       * in the signature. If they are equal then we can trust the IdP.
       */
      for (IDPSSODescriptorType idpInfo : idpInfos) {
        KeyDescriptorType[] keys = idpInfo.getKeyDescriptorArray();

        // IDPSSODescriptor/KeyDescriptor
        for (KeyDescriptorType key : keys) {
          if (key.getKeyInfo() != null) {
            // IDPSSODescriptor/KeyDescriptor/KeyInfo
            if (key.getKeyInfo().getX509DataArray() != null) {
              X509DataType[] x509s = key.getKeyInfo().getX509DataArray();

              // IDPSSODescriptor/KeyDescriptor/KeyInfo/X509Data
              for (X509DataType x509 : x509s) {
                if (x509.getX509CertificateArray() != null) {
                  byte[][] x509bytesArray = x509.getX509CertificateArray();
                  
                  // IDPSSODescriptor/KeyDescriptor/KeyInfo/X509Data/X509Certificate
                  try {
                    CertificateFactory certFactory = CertificateFactory.getInstance("x.509");

                    for (byte[] x509bytes : x509bytesArray) {
                      ByteArrayInputStream certByteStream = new ByteArrayInputStream(x509bytes);
                      X509Certificate x509CertFromMetadata = (X509Certificate)certFactory.generateCertificate(certByteStream);
                      certByteStream.close();

                      X509Certificate x509CertFromSig = getX509CertFromSignature(samlResponse);
                      
                      /* Compare the X509 in the metadata to the X509 in the signature.
                       * If they match then we can stop verifying and trust the IdP.
                       */
                      if (x509CertFromSig.equals(x509CertFromMetadata)) {
                        return true;
                      }
                    } // for (byte[] x509bytes : x509bytesArray)
                  }
                  catch(CertificateException ce) {
                    logger.error("Error obtaining certificate factory", ce);
                    throw new GuanxiException(ce);
                  }
                  catch(IOException ioe) {
                    logger.error("Error closing certificate byte stream", ioe);
                    throw new GuanxiException(ioe);
                  }
                } // if (x509.getX509CertificateArray() != null)
              }
            }
          }

          /* Direct signature validation via RSAKeyValue
           * @todo implement this
           */
        }
      } // for (IDPSSODescriptorType idpInfo : idpInfos)
    } // if (saml.getIDPSSODescriptorArray() != null)

    /* PKIX Path Validation
     * quickie summary:
     * - Match X509 in SAML Response signature to KeyName in IdP metadata
     * - Match issuer of X509 in SAML Response to one of the X509s in shibmeta:keyauthority
     *   in global metadata
     */
    X509Certificate x509CertFromSig = getX509CertFromSignature(samlResponse);

    // First find a match between the X509 in the signature and a KeyName in the metadata...
    if (matchCertToKeyName(x509CertFromSig, saml)) {
      // ...then follow the chain from the X509 in the signature back to a supported CA in the metadata
      if (validateCertPath(x509CertFromSig)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Verifies the digital signature on a SAML Response
   *
   * @param samlResponse The SAML Response document containing the signature
   * @return true if the signature verifies otherwise false
   * @throws GuanxiException if an error occurs
   */
  private boolean verifySignature(ResponseDocument samlResponse) throws GuanxiException {
    try {
      // Get the signature on the SAML response...
      SignatureType sig = samlResponse.getResponse().getSignature();
      if (sig != null) {
        // ...and verify it
        XMLSignature signature = new XMLSignature((Element)sig.getDomNode(),"");
        KeyInfo keyInfo = signature.getKeyInfo();
        if (keyInfo != null) {
          if(keyInfo.containsX509Data()) {
            X509Certificate cert = signature.getKeyInfo().getX509Certificate();
            if(cert != null) {
              return signature.checkSignatureValue(cert);
            }
          }
        }
      }
    }
    catch(XMLSecurityException xse) {
      logger.error("Error translating signature from DOM to XMLSignature", xse);
      throw new GuanxiException(xse);
    }

    return false;
  }

  /**
   * Retrieves the X509Certificate from a digital signature
   *
   * @param samlResponse The SAML Response containing the signature
   * @return X509Certificate from the signature
   * @throws GuanxiException if an error occurs
   */
  private X509Certificate getX509CertFromSignature(ResponseDocument samlResponse) throws GuanxiException {
    try {
      KeyInfoType keyInfo = samlResponse.getResponse().getSignature().getKeyInfo();
      byte[] x509CertBytes = keyInfo.getX509DataArray(0).getX509CertificateArray(0);
      CertificateFactory certFactory = CertificateFactory.getInstance("x.509");
      ByteArrayInputStream certByteStream = new ByteArrayInputStream(x509CertBytes);
      X509Certificate cert = (X509Certificate)certFactory.generateCertificate(certByteStream);
      certByteStream.close();
      return cert;
    }
    catch(CertificateException ce) {
      logger.error("Error obtaining certificate factory", ce);
      throw new GuanxiException(ce);
    }
    catch(IOException ioe) {
      logger.error("Error closing certificate byte stream", ioe);
      throw new GuanxiException(ioe);
    }
  }

  /**
   * Tries to match an X509 certificate subject to a KeyName in metadata
   *
   * @param x509 The X509 to match with a KeyName
   * @param saml The metadata which contains the KeyName
   * @return true if a match was made, otherwise false
   */
  private boolean matchCertToKeyName(X509Certificate x509, EntityDescriptorType saml) {
    IDPSSODescriptorType[] idpSSOs = saml.getIDPSSODescriptorArray();

    // EntityDescriptor/IDPSSODescriptor
    for (IDPSSODescriptorType idpSSO : idpSSOs) {
      // EntityDescriptor/IDPSSODescriptor/KeyDescriptor
      KeyDescriptorType[] keyDescriptors = idpSSO.getKeyDescriptorArray();

      for (KeyDescriptorType keyDescriptor : keyDescriptors) {
        // EntityDescriptor/IDPSSODescriptor/KeyDescriptor/KeyInfo
        if (keyDescriptor.getKeyInfo() != null) {
          // EntityDescriptor/IDPSSODescriptor/KeyDescriptor/KeyInfo/KeyName
          if (keyDescriptor.getKeyInfo().getKeyNameArray() != null) {
            String[] keyNames = keyDescriptor.getKeyInfo().getKeyNameArray();

            for (String keyName : keyNames) {
              String metadataKeyName = new String(keyName.getBytes());

              // Do the hard work of comparison
              if (compareX509SubjectWithKeyName(x509, metadataKeyName)) {
                return true;
              }
            }
          }
        }
      }
    }

    return false;
  }

  /**
   * Compares an X509 subject to a KeyName string using various techniques.
   * We can hit a problem with certs when the IdP's providerId is, e.g. urn:uni:ac:uk:idp
   * but it's cert DN is CN=urn:uni:ac:uk:idp, OU=Unknown, O=Unknown, L=Unknown, ST=Unknown, C=Unknown
   * We didn't see this in the beginning as the certs generated by BouncyCastle in the IdP don't have
   * the extra OU etc. Most commandline tools do put the extra OU in if you don't specify them.
   *
   * @param x509 The X509 to use
   * @param keyName The KeyName string to use
   * @return if they match, otherwise false
   */
  private boolean compareX509SubjectWithKeyName(X509Certificate x509, String keyName) {
    logger.debug("subject DN : " + x509.getSubjectDN().getName() + ", KeyName : " + keyName);

    // Try the full DN
    logger.debug("trying DN : " + x509.getSubjectDN().getName() + " KeyName : " + keyName);
    if (x509.getSubjectDN().getName().equals(keyName)) {
      logger.debug("matched DN");
      return true;
    }

    // Try the CN
    String cn = x509.getSubjectDN().getName().split(",")[0].split("=")[1];
    logger.debug("trying CN : " + cn + " KeyName : " + keyName);
    if (cn.equals(keyName)) {
      logger.debug("matched CN");
      return true;
    }

    return false;
  }

  /**
   * Validates a certificate path starting with the mystery cert and working
   * back to a trust anchor, using the CA certs in the trust engine.
   *
   * @param x509ToVerify the mystery cert, should we trust it?
   * @return true if we trust the cert, otherwise false
   */
  private boolean validateCertPath(X509Certificate x509ToVerify) {
    for (X509Certificate caX509 : caCerts) {
      if (caX509.getSubjectDN().getName().equals(x509ToVerify.getIssuerDN().getName())) {
        return true;
      }
    }

    return false;
  }
}
