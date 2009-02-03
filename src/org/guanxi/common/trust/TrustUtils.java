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

package org.guanxi.common.trust;

import org.guanxi.xal.saml_2_0.metadata.*;
import org.guanxi.xal.w3.xmldsig.X509DataType;
import org.guanxi.xal.w3.xmldsig.KeyInfoType;
import org.guanxi.xal.saml_1_0.protocol.ResponseDocument;
import org.guanxi.common.GuanxiException;
import org.apache.log4j.Logger;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.utils.Constants;
import org.w3c.dom.*;
import org.bouncycastle.openssl.PEMReader;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;
import java.util.Arrays;
import java.net.URL;
import java.net.MalformedURLException;

/**
 * Shibboleth and SAML trust functionality
 *
 * @author alistair
 * @author matthew
 */
public class TrustUtils {
  /** Our logger */
  private static final Logger logger = Logger.getLogger(TrustUtils.class.getName());
  /**
   * This holds the path to the Reference node of a signed SAML Response.
   * This is used to validate the signature in the SAML Response.
   */
  private static XPathExpression referencePath;

  // Setup the XPath stuff and precompile any expressions
  static {
    XPathFactory factory = XPathFactory.newInstance();
    XPath xPath = factory.newXPath();
    xPath.setNamespaceContext(new SAMLNamespaceContext());
    try {
      referencePath = xPath.compile("//ds:Signature/ds:SignedInfo/ds:Reference");
    }
    catch (XPathExpressionException e) {
      throw new ExceptionInInitializerError(e);
    }
  }

  /**
   * Performs trust validation via X509 certificates embedded in metadata. The trust is in the context
   * of a secure connection to an AA.
   *
   * @param saml2Metadata The metadata for the SP
   * @param clientCerts The SP's client certificates from the secure connection
   * @return true if validation succeeds otherwise false
   * @throws GuanxiException if an error occurs
   */
  public static boolean validateClientCert(EntityDescriptorType saml2Metadata, X509Certificate[] clientCerts) throws GuanxiException {
    X509Certificate[] x509sFromMetadata = getX509CertsFromSPMetadata(saml2Metadata);

    // Try validation via direct X509 in metadata
    for (X509Certificate clientCert : clientCerts) {
      for (X509Certificate x509FromMetadata : x509sFromMetadata) {
        if (x509FromMetadata.equals(clientCert)) {
          return true;
        }
      }
    }

    // Try validation via PKIX path validation
    String[] keyNames = getKeyNamesFromSPMetadata(saml2Metadata);
    for (String keyName : keyNames) {
      for (X509Certificate clientCert : clientCerts) {
        if (compareX509SubjectWithKeyName(clientCert, keyName)) {
          return true;
        }
      }
    }

    return false;
  }

  /**
   * Performs trust validation via X509 certificates embedded in metadata. The trust is in the context
   * of a SAML Response coming from an IdP.
   *
   * @param samlResponse The SAML Response from an IdP containing an AuthenticationStatement
   * @param saml2Metadata The metadata for the IdP
   * @return true if validation succeeds otherwise false
   * @throws GuanxiException if an error occurs
   */
  public static boolean validateWithEmbeddedCert(ResponseDocument samlResponse, EntityDescriptorType saml2Metadata) throws GuanxiException {
    X509Certificate[] x509sFromMetadata = getX509CertsFromIdPMetadata(saml2Metadata);
    X509Certificate x509CertFromSig = getX509CertFromSignature(samlResponse);

    for (X509Certificate x509FromMetadata : x509sFromMetadata) {
      if (x509CertFromSig.equals(x509FromMetadata)) {
        return true;
      }
    }

    /* Direct signature validation via RSAKeyValue
     * @todo implement this
     */

    return false;
  }

  /**
   * Performs PKIX path validation based on certificates from metadata
   *
   * @param samlResponse The SAML Response from an IdP containing an AuthenticationStatement
   * @param saml2Metadata The metadata for the IdP
   * @param caCerts The list of CA root certs as trust anchors
   * @return true if validation succeeds otherwise false
   * @throws GuanxiException if an error occurs
   */
  public static boolean validatePKIX(ResponseDocument samlResponse, EntityDescriptorType saml2Metadata,
                                     Vector<X509Certificate> caCerts) throws GuanxiException {
    /* PKIX Path Validation
     * quickie summary:
     * - Match X509 in SAML Response signature to KeyName in IdP metadata
     * - Match issuer of X509 in SAML Response to one of the X509s in shibmeta:keyauthority
     *   in global metadata
     */
    X509Certificate x509CertFromSig = getX509CertFromSignature(samlResponse);

    // First find a match between the X509 in the signature and a KeyName in the metadata...
    if (matchCertToKeyName(x509CertFromSig, saml2Metadata)) {
      // ...then follow the chain from the X509 in the signature back to a supported CA in the metadata
      if (validateCertPath(x509CertFromSig, caCerts)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Performs PKIX path validation based on certificates from a back channel connection
   *
   * @param x509CertFromConnection The certificate from the connection
   * @param saml2Metadata The metadata for the IdP
   * @param caCerts The list of CA root certs as trust anchors
   * @return true if validation succeeds otherwise false
   * @throws GuanxiException if an error occurs
   */
  public static boolean validatePKIXBC(X509Certificate x509CertFromConnection,
                                       EntityDescriptorType saml2Metadata,
                                       Vector<X509Certificate> caCerts) throws GuanxiException {
    /* PKIX Path Validation
     * quickie summary:
     * - Match X509 from connection to KeyName in IdP metadata
     * - Match issuer of X509 from connection to one of the X509s in shibmeta:keyauthority
     *   in global metadata
     */

    // First find a match between the X509 from the connection and a KeyName in the metadata...
    if (matchAACertToKeyName(x509CertFromConnection, saml2Metadata)) {
      // ...then follow the chain from the X509 in the signature back to a supported CA in the metadata
      if (validateCertPath(x509CertFromConnection, caCerts)) {
        return true;
      }
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
  public static X509Certificate getX509CertFromSignature(ResponseDocument samlResponse) throws GuanxiException {
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
   * @param saml2Metadata The metadata which contains the KeyName
   * @return true if a match was made, otherwise false
   */
  public static boolean matchCertToKeyName(X509Certificate x509, EntityDescriptorType saml2Metadata) {
    IDPSSODescriptorType[] idpSSOs = saml2Metadata.getIDPSSODescriptorArray();

    // EntityDescriptor/IDPSSODescriptor
    for (IDPSSODescriptorType idpSSO : idpSSOs) {
      // EntityDescriptor/IDPSSODescriptor/KeyDescriptor
      if (validateX509WithKeyName(x509, idpSSO.getKeyDescriptorArray())) {
        return true;
      }
    }

    return false;
  }

  /**
   * Tries to match an X509 certificate subject to a KeyName in metadata
   *
   * @param x509 The X509 to match with a KeyName
   * @param saml2Metadata The metadata which contains the KeyName
   * @return true if a match was made, otherwise false
   */
  public static boolean matchAACertToKeyName(X509Certificate x509, EntityDescriptorType saml2Metadata) {
    AttributeAuthorityDescriptorType[] aaList = saml2Metadata.getAttributeAuthorityDescriptorArray();

    // EntityDescriptor/AttributeAuthorityDescriptor
    for (AttributeAuthorityDescriptorType aa : aaList) {
      // EntityDescriptor/IDPSSODescriptor/KeyDescriptor
      if (validateX509WithKeyName(x509, aa.getKeyDescriptorArray())) {
        return true;
      }
    }

    return false;
  }

  /**
   * Validates an X509 certificate based on key names in metadata
   *
   * @param x509 The X509 to match with a KeyName
   * @param keyDescriptors pointer to the list of key descriptors from the metadata
   * @return if a match was found
   */
  public static boolean validateX509WithKeyName(X509Certificate x509, KeyDescriptorType[] keyDescriptors) {
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
  public static boolean compareX509SubjectWithKeyName(X509Certificate x509, String keyName) {
    logger.debug("subject DN : " + x509.getSubjectDN().getName() + ", KeyName : " + keyName);

    // Try the full DN
    logger.debug("trying DN : " + x509.getSubjectDN().getName() + " KeyName : " + keyName);
    if (x509.getSubjectDN().getName().equals(keyName)) {
      logger.debug("matched DN");
      return true;
    }

    // Try the CN
    String[] split;
    for (String currentEntry : x509.getSubjectDN().getName().split(",\\s*")) {
      split = currentEntry.split("=");
      if (split[0].equals("CN") && split[1].equals(keyName)) {
        logger.debug("matched CN");
        return true;
      }
    }
    
    return false;
  }

  /**
   * Validates a certificate path starting with the mystery cert and working
   * back to a trust anchor, using the CA certs in the trust engine.
   *
   * @param x509ToVerify the mystery cert, should we trust it?
   * @param caCerts the list of CA root certs to trust
   * @return true if we trust the cert, otherwise false
   */
  public static boolean validateCertPath(X509Certificate x509ToVerify, Vector<X509Certificate> caCerts) {
    for (X509Certificate caX509 : caCerts) {
      if (caX509.getSubjectDN().getName().equals(x509ToVerify.getIssuerDN().getName())) {
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
  public static boolean verifySignature(ResponseDocument samlResponse) throws GuanxiException {
    try {
      /* We need to check for ID attributes, which requires DOM Level 3, which XMLBeans
       * does not support. So we need to jump into DOM land.
       */
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      dbf.setNamespaceAware(true);
      dbf.setAttribute("http://xml.org/sax/features/namespaces", Boolean.TRUE);
      DocumentBuilder db = dbf.newDocumentBuilder();
      db.setErrorHandler(new org.apache.xml.security.utils.IgnoreAllErrorHandler());
      Document doc = db.parse(samlResponse.newInputStream());

      setIdNode(doc);

      NodeList nodes = doc.getElementsByTagNameNS(Constants.SignatureSpecNS, "Signature");
      Element sigElement = (Element)nodes.item(0);
      XMLSignature xmlSignature = new XMLSignature(sigElement, "");
      X509Certificate cert = xmlSignature.getKeyInfo().getX509Certificate();

      return xmlSignature.checkSignatureValue(cert);
    }
    catch(ParserConfigurationException pce) {
      throw new GuanxiException(pce);
    }
    catch(SAXException se) {
      throw new GuanxiException(se);
    }
    catch(IOException ioe) {
      throw new GuanxiException(ioe);
    }
    catch(XMLSecurityException xse) {
      throw new GuanxiException(xse);
    }
  }
  
  /**
   * This passes through the nodes of the document looking for the Reference Id and
   * then assigns that Id to the root node of the document.
   * 
   * @param doc SAML Response document
   * @throws GuanxiException if an error occurs
   */
  private static void setIdNode(Document doc) throws GuanxiException {
    try {
      // Look for the Reference node in the Signature...
      NodeList sigReference = (NodeList)referencePath.evaluate(doc, XPathConstants.NODESET);

      // ...to see if it has a value...
      if (sigReference.getLength() > 0 &&
          sigReference.item(0).getAttributes() != null &&
          sigReference.item(0).getAttributes().getNamedItem("URI") != null &&
          sigReference.item(0).getAttributes().getNamedItem("URI").getTextContent() != "") {
        // ...and mark the attribute with that value as an ID attribute
        doc.getDocumentElement().setIdAttribute("ResponseID", true);
      }
    }
    catch(XPathExpressionException xee) {
      throw new GuanxiException(xee);
    }
  }

  /**
   * Extracts X509 certificates from a SAML2 IdP EntityDescriptor
   *
   * @param saml2Metadata The SAML2 metadata which may contain the certificates
   * @return array of X509Certificate objects created from the metadata
   * @throws GuanxiException if an error occurs
   */
  public static X509Certificate[] getX509CertsFromIdPMetadata(EntityDescriptorType saml2Metadata) throws GuanxiException {
    return getX509CertsFromMetadata(saml2Metadata.getIDPSSODescriptorArray());
  }

  /**
   * Extracts X509 certificates from a SAML2 SP EntityDescriptor
   *
   * @param saml2Metadata The SAML2 metadata which may contain the certificates
   * @return array of X509Certificate objects created from the metadata
   * @throws GuanxiException if an error occurs
   */
  public static X509Certificate[] getX509CertsFromSPMetadata(EntityDescriptorType saml2Metadata) throws GuanxiException {
    return getX509CertsFromMetadata(saml2Metadata.getSPSSODescriptorArray());
  }

  /**
   * Extracts X509 certificates from a SAML2 EntityDescriptor
   *
   * @param entityDescriptors The SAML2 metadata which may contain the certificates. This can either be
   * an IDPSSODescriptor or an SPSSODescriptor
   * @return array of X509Certificate objects created from the metadata
   * @throws GuanxiException if an error occurs
   */
  public static X509Certificate[] getX509CertsFromMetadata(SSODescriptorType[] entityDescriptors) throws GuanxiException {
    if (entityDescriptors == null) {
      return null;
    }

    Vector<X509Certificate> x509Certs = new Vector<X509Certificate>();

      for (SSODescriptorType entityDescriptor : entityDescriptors) {
        KeyDescriptorType[] keys = entityDescriptor.getKeyDescriptorArray();

        // SSODescriptor/KeyDescriptor
        for (KeyDescriptorType key : keys) {
          if (key.getKeyInfo() != null) {
            // SSODescriptor/KeyDescriptor/KeyInfo
            if (key.getKeyInfo().getX509DataArray() != null) {
              X509DataType[] x509s = key.getKeyInfo().getX509DataArray();

              // SSODescriptor/KeyDescriptor/KeyInfo/X509Data
              for (X509DataType x509 : x509s) {
                if (x509.getX509CertificateArray() != null) {
                  byte[][] x509bytesArray = x509.getX509CertificateArray();

                  // SSODescriptor/KeyDescriptor/KeyInfo/X509Data/X509Certificate
                  try {
                    CertificateFactory certFactory = CertificateFactory.getInstance("x.509");

                    for (byte[] x509bytes : x509bytesArray) {
                      ByteArrayInputStream certByteStream = new ByteArrayInputStream(x509bytes);
                      X509Certificate x509CertFromMetadata = (X509Certificate)certFactory.generateCertificate(certByteStream);
                      certByteStream.close();

                      x509Certs.add(x509CertFromMetadata);
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
        }
      } // for (SSODescriptorType idpInfo : idpInfos)

    X509Certificate[] x509sFromMetadata = new X509Certificate[x509Certs.size()];
    x509Certs.copyInto(x509sFromMetadata);
    return x509sFromMetadata;
  }

  /**
   * Extracts KeyNames from a SAML2 IdP EntityDescriptor
   *
   * @param saml2Metadata The SAML2 metadata which may contain the certificates
   * @return array of String objects created from the metadata
   * @throws GuanxiException if an error occurs
   */
  public static String[] getKeyNamesFromIdPMetadata(EntityDescriptorType saml2Metadata) throws GuanxiException {
    return getKeyNamesFromMetadata(saml2Metadata.getIDPSSODescriptorArray());
  }

  /**
   * Extracts KeyNames from a SAML2 SP EntityDescriptor
   *
   * @param saml2Metadata The SAML2 metadata which may contain the certificates
   * @return array of String objects created from the metadata
   * @throws GuanxiException if an error occurs
   */
  public static String[] getKeyNamesFromSPMetadata(EntityDescriptorType saml2Metadata) throws GuanxiException {
    return getKeyNamesFromMetadata(saml2Metadata.getSPSSODescriptorArray());
  }

  /**
   * Extracts KeyNames from a SAML2 EntityDescriptor
   *
   * @param entityDescriptors The SAML2 metadata which may contain the key names. This can either be
   * an IDPSSODescriptor or an SPSSODescriptor
   * @return array of String objects created from the metadata
   * @throws GuanxiException if an error occurs
   */
  public static String[] getKeyNamesFromMetadata(SSODescriptorType[] entityDescriptors) throws GuanxiException {
    if (entityDescriptors == null) {
      return null;
    }

    Vector<String> keyNames = new Vector<String>();

      for (SSODescriptorType entityDescriptor : entityDescriptors) {
        KeyDescriptorType[] keys = entityDescriptor.getKeyDescriptorArray();

        // SSODescriptor/KeyDescriptor
        for (KeyDescriptorType key : keys) {
          if (key.getKeyInfo() != null) {
            // SSODescriptor/KeyDescriptor/KeyInfo
            if (key.getKeyInfo().getKeyNameArray() != null) {
              keyNames.addAll(Arrays.asList(key.getKeyInfo().getKeyNameArray()));
            }
          }
        }
      } // for (SSODescriptorType idpInfo : idpInfos)

    String[] keyNamesFromMetadata = new String[keyNames.size()];
    keyNames.copyInto(keyNamesFromMetadata);
    return keyNamesFromMetadata;
  }

  /**
   * Compares the fingerprints of two X509 certificates.
   *
   * @param cert1 X509Certificate
   * @param cert2 X509Certificate
   * @return true if the fingerprints are equal, otherwise false
   * @throws GuanxiException if an error occurred
   */
  public static boolean checkCertfingerprints(X509Certificate cert1, X509Certificate cert2) throws GuanxiException {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA1");

      md.update(cert1.getEncoded());
      byte[] cert1Fingerprint = md.digest();

      md.update(cert2.getEncoded());
      byte[] cert2Fingerprint = md.digest();

      return byteArrayToHexString(cert1Fingerprint).equals(byteArrayToHexString(cert2Fingerprint));
    }
    catch(NoSuchAlgorithmException nsae) {
      throw new GuanxiException(nsae);
    }
    catch(CertificateEncodingException cee) {
      throw new GuanxiException(cee);
    }
  }

  /**
   * Returns the hex representation of a byte array.
   * 
   * @param bytes byte array to be converted to hex
   * @return hex representation of bytes
   */
  public static String byteArrayToHexString(byte bytes[]) {
    byte ch = 0x00;
    int count = 0;

    if (bytes == null || bytes.length <= 0) return null;

    String pseudo[] = {"0", "1", "2",
                       "3", "4", "5", "6", "7", "8",
                       "9", "A", "B", "C", "D", "E",
                       "F"};

    StringBuffer out = new StringBuffer(bytes.length * 2);

    while (count < bytes.length) {
      ch = (byte) (bytes[count] & 0xF0);
      ch = (byte) (ch >>> 4);
      ch = (byte) (ch & 0x0F);
      out.append(pseudo[ (int) ch]);
      ch = (byte) (bytes[count] & 0x0F);
      out.append(pseudo[ (int) ch]);

      if (count < (bytes.length -1)) {
        out.append(":");
      }

      count++;
    }

    return new String(out);
  }

  /**
   * Converts a PEM to an X509Certificate. Requires the Bouncy Castle provider
   * to be installed.
   *
   * @param pemURL URL of the PEM file
   * @return X509Certificate
   * @throws GuanxiException if an error occurs
   */
  public static X509Certificate pem2x509(String pemURL) throws GuanxiException {
    try {
      URL pem = new URL(pemURL);
      PEMReader pemReader = new PEMReader(new InputStreamReader(pem.openStream()));
      return (X509Certificate)pemReader.readObject();
    }
    catch(MalformedURLException mue) {
      throw new GuanxiException(mue);
    }
    catch(IOException ioe) {
      throw new GuanxiException(ioe);
    }
  }
}

