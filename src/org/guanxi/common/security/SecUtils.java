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
//: The Initial Developer of the Original Code is Alistair Young alistair@smo.uhi.ac.uk.
//: Portions created by SMO WWW Development Group are Copyright (C) 2005 SMO WWW Development Group.
//: All Rights Reserved.
//:
/* CVS Header
   $Id$
   $Log$
   Revision 1.3  2005/07/11 10:52:05  alistairskye
   Package restructure

   Revision 1.2  2005/05/11 13:11:53  alistairskye
   Updated sign() to use new keyType in configNode to support DSA/RSA

   Revision 1.1  2005/05/04 13:29:00  alistairskye
   Moved here from org.Guanxi.SAMUEL.Utils

   Revision 1.1  2005/04/28 13:32:09  alistairskye
   Security utils for SAMUEL generated SAML messages

*/

package org.guanxi.common.security;

import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.transforms.params.InclusiveNamespaces;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.guanxi.samuel.utils.XUtils;
import org.guanxi.common.SOAPUtils;
import java.security.*;
import java.security.cert.X509Certificate;
import java.security.cert.CertificateException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * <font size=5><b></b></font>
 *
 * @author Alistair Young alistair@smo.uhi.ac.uk
 */
public class SecUtils {
  static private SecUtils _instance = null;

  static public SecUtils getInstance() {
    if (_instance == null) {
      synchronized(SOAPUtils.class) {
        if (_instance == null) {
          _instance = new SecUtils();
        }
      }
    }

    return _instance;
  }

  private SecUtils() {
    org.apache.xml.security.Init.init();
  }

  public Document sign(Node configNode, Document inDocToSign, String inElementToSign) {
    XUtils xUtils = XUtils.getInstance();
    String keystoreType = xUtils.getNodeValue(configNode, "keystore-type");
    String keystoreFile = xUtils.getNodeValue(configNode, "keystore-file");
    String keystorePass = xUtils.getNodeValue(configNode, "keystore-password");
    String privateKeyAlias = xUtils.getNodeValue(configNode, "private-key-alias");
    String privateKeyPass = xUtils.getNodeValue(configNode, "private-key-password");
    String certificateAlias = xUtils.getNodeValue(configNode, "certificate-alias");
    String keyType = xUtils.getNodeValue(configNode, "key-type");

    if (keyType.equalsIgnoreCase("dsa")) keyType = XMLSignature.ALGO_ID_SIGNATURE_DSA;
    if (keyType.equalsIgnoreCase("rsa")) keyType = XMLSignature.ALGO_ID_SIGNATURE_RSA;

    try {
      //Constants.setSignatureSpecNSprefix("ds");

      KeyStore ks = null;
      ks = KeyStore.getInstance(keystoreType);

      FileInputStream fis = null;
      fis = new FileInputStream(keystoreFile);

      ks.load(fis, keystorePass.toCharArray());

      PrivateKey privateKey = null;
      privateKey = (PrivateKey)ks.getKey(privateKeyAlias, privateKeyPass.toCharArray());

      XMLSignature sig = null;
      sig = new XMLSignature(inDocToSign, "", keyType, Canonicalizer.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);

      // For Shibboleth 1.2.1, <ds:Signature> must be the first element after the root
      inDocToSign.getDocumentElement().insertBefore(sig.getElement(), inDocToSign.getDocumentElement().getFirstChild());

      Transforms transforms = new Transforms(sig.getDocument());

      /* First we have to strip away the signature element (it's not part of the
       * signature calculations). The enveloped transform can be used for this.
       */
      transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);
      transforms.addTransform(Transforms.TRANSFORM_C14N_EXCL_OMIT_COMMENTS);
      transforms.item(1).getElement().appendChild(inDocToSign.createTextNode(new InclusiveNamespaces(inDocToSign, "#default saml samlp ds code kind rw typens").getInclusiveNamespaces()));

      sig.addDocument(inElementToSign, transforms);

      //Add in the KeyInfo for the certificate that we used the private key of
      X509Certificate cert = null;
      cert = (X509Certificate)ks.getCertificate(certificateAlias);

      sig.addKeyInfo(cert);

      sig.addKeyInfo(cert.getPublicKey());

      sig.sign(privateKey);
    }
    catch(XMLSecurityException xse) {}
    catch(KeyStoreException kse) {}
    catch(FileNotFoundException fnfe) {}
    catch(NoSuchAlgorithmException nsae) {}
    catch(CertificateException ce) {}
    catch(IOException ioe) {}
    catch(UnrecoverableKeyException urke) {}

    return inDocToSign;
  }
}
