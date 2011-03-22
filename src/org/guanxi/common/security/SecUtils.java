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

package org.guanxi.common.security;

import org.w3c.dom.Document;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.transforms.params.InclusiveNamespaces;
import org.guanxi.common.GuanxiException;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.security.*;
import java.security.cert.X509Certificate;
import java.io.*;
import java.util.Hashtable;
import java.util.Date;
import java.util.Random;
import java.util.Vector;
import java.math.BigInteger;

/**
 * <font size=5><b></b></font>
 *
 * @author Alistair Young alistair@smo.uhi.ac.uk
 */
public class SecUtils {
  static private SecUtils _instance = null;

  static public SecUtils getInstance() {
    if (_instance == null) {
      synchronized(SecUtils.class) {
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

  public Document sign(SecUtilsConfig config, Document inDocToSign, String inElementToSign) throws GuanxiException {
    String keystoreType = config.getKeystoreType();
    String keystoreFile = config.getKeystoreFile();
    String keystorePass = config.getKeystorePass();
    String privateKeyAlias = config.getPrivateKeyAlias();
    String privateKeyPass = config.getPrivateKeyPass();
    String certificateAlias = config.getCertificateAlias();

    try {
      KeyStore ks = null;
      ks = KeyStore.getInstance(keystoreType);

      FileInputStream fis = null;
      fis = new FileInputStream(keystoreFile);

      ks.load(fis, keystorePass.toCharArray());
      fis.close();

      PrivateKey privateKey = null;
      privateKey = (PrivateKey)ks.getKey(privateKeyAlias, privateKeyPass.toCharArray());

      String keyType = privateKey.getAlgorithm();
      if (keyType.equalsIgnoreCase("dsa")) keyType = XMLSignature.ALGO_ID_SIGNATURE_DSA;
      if (keyType.equalsIgnoreCase("rsa")) keyType = XMLSignature.ALGO_ID_SIGNATURE_RSA;

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
      /* Have to use a text node for compatibility with Internet2 Shibboleth SP 1.3
       * otherwise the signature fails to verify. The correct way do add it is:
       * transforms.item(1).getElement().appendChild(new InclusiveNamespaces(inDocToSign, "#default saml samlp ds code kind rw typens").getElement());
       */
      transforms.item(1).getElement().appendChild(inDocToSign.createTextNode(new InclusiveNamespaces(inDocToSign, "#default saml samlp ds code kind rw typens").getInclusiveNamespaces()));

      sig.addDocument(inElementToSign, transforms);

      //Add in the KeyInfo for the certificate that we used the private key of
      X509Certificate cert = null;
      cert = (X509Certificate)ks.getCertificate(certificateAlias);

      sig.addKeyInfo(cert);

      sig.addKeyInfo(cert.getPublicKey());

      sig.sign(privateKey);
    }
    catch(Exception e) {
      throw new GuanxiException(e);
    }

    return inDocToSign;
  }

  public Document saml2Sign(SecUtilsConfig config, Document inDocToSign, String elementIDToSign) throws GuanxiException {
    String keystoreType = config.getKeystoreType();
    String keystoreFile = config.getKeystoreFile();
    String keystorePass = config.getKeystorePass();
    String privateKeyAlias = config.getPrivateKeyAlias();
    String privateKeyPass = config.getPrivateKeyPass();
    String certificateAlias = config.getCertificateAlias();

    try {
      KeyStore ks = null;
      ks = KeyStore.getInstance(keystoreType);

      FileInputStream fis = null;
      fis = new FileInputStream(keystoreFile);

      ks.load(fis, keystorePass.toCharArray());
      fis.close();

      PrivateKey privateKey = null;
      privateKey = (PrivateKey)ks.getKey(privateKeyAlias, privateKeyPass.toCharArray());

      String keyType = privateKey.getAlgorithm();
      if (keyType.equalsIgnoreCase("dsa")) keyType = XMLSignature.ALGO_ID_SIGNATURE_DSA;
      if (keyType.equalsIgnoreCase("rsa")) keyType = XMLSignature.ALGO_ID_SIGNATURE_RSA;

      XMLSignature sig = null;
      sig = new XMLSignature(inDocToSign, "", keyType, Canonicalizer.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);

      // For SAML2, <ds:Signature> must be the first element after the Issuer
      NodeList nodes = inDocToSign.getDocumentElement().getChildNodes();
      Node issuerNode = null;
      for (int c=0; c < nodes.getLength(); c++) {
        issuerNode = nodes.item(c);
        if (issuerNode.getLocalName() != null) {
          if (issuerNode.getLocalName().equals("Issuer")) {
            break;
          }
        }
      }
      inDocToSign.getDocumentElement().insertBefore(sig.getElement(), issuerNode.getNextSibling().getNextSibling());

      Transforms transforms = new Transforms(sig.getDocument());

      /* First we have to strip away the signature element (it's not part of the
       * signature calculations). The enveloped transform can be used for this.
       */
      transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);
      transforms.addTransform(Transforms.TRANSFORM_C14N_EXCL_OMIT_COMMENTS);
      /* Have to use a text node for compatibility with Internet2 Shibboleth SP 1.3
       * otherwise the signature fails to verify. The correct way do add it is:
       * transforms.item(1).getElement().appendChild(new InclusiveNamespaces(inDocToSign, "#default saml samlp ds code kind rw typens").getElement());
       */
      transforms.item(1).getElement().appendChild(inDocToSign.createTextNode(new InclusiveNamespaces(inDocToSign, "#default saml samlp ds code kind rw typens").getInclusiveNamespaces()));

      if ((elementIDToSign != null) && (!elementIDToSign.equals(""))) {
        sig.addDocument("#" + elementIDToSign, transforms);
      }
      else {
        sig.addDocument("", transforms);
      }

      //Add in the KeyInfo for the certificate that we used the private key of
      X509Certificate cert = null;
      cert = (X509Certificate)ks.getCertificate(certificateAlias);

      sig.addKeyInfo(cert);

      sig.addKeyInfo(cert.getPublicKey());

      sig.sign(privateKey);
    }
    catch(Exception e) {
      throw new GuanxiException(e);
    }

    return inDocToSign;
  }

  public String encrypt(String data) {
    try {
      char[] hexChars ={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
      MessageDigest md5 = MessageDigest.getInstance("MD5");
      md5.reset();
      md5.update(data.getBytes());
      byte[] hashBytes = md5.digest();
      String hex = "";
      int msb;
      int lsb = 0;
      int i;
      for (i = 0; i < hashBytes.length; i++) {
        msb = ((int)hashBytes[i] & 0x000000FF) / 16;
        lsb = ((int)hashBytes[i] & 0x000000FF) % 16;
        hex = hex + hexChars[msb] + hexChars[lsb];
      }
      return(hex);
    }
    catch (NoSuchAlgorithmException e) {
      return null;
    }
  }

  /**
   * Generates a self signed public/private key pair and puts them and the associated certificate in
   * a KeyStore.
   *
   * @param cn The CN of the X509 containing the public key, e.g. "cn=guanxi_sp,ou=smo,o=uhi"
   * @param keystoreFile The full path and name of the KeyStore to create or add the certificate to
   * @param keystorePassword The password for the KeyStore
   * @param privateKeyPassword The password for the private key associated with the public key certificate
   * @param privateKeyAlias The alias under which the private key will be stored
   * @param keyType The type of key, RSA or DSA
   * @throws GuanxiException if an error occurred
   */
  public void createSelfSignedKeystore(String cn, String keystoreFile, String keystorePassword,
                                       String privateKeyPassword, String privateKeyAlias,
                                       String keyType) throws GuanxiException {
    try {
      KeyStore ks = KeyStore.getInstance("JKS");

      // Does the keystore exist?
      File keyStore = new File(keystoreFile);
      if (keyStore.exists()) {
        FileInputStream fis = new FileInputStream(keystoreFile);
        ks.load(fis, keystorePassword.toCharArray());
        fis.close();
      }
      else
        ks.load(null, null);

      // Generate a new public/private key pair
      KeyPairGenerator keyGen = null;
      if (keyType.toLowerCase().equals("rsa")) {
        keyGen = KeyPairGenerator.getInstance("RSA");
      }
      else if (keyType.toLowerCase().equals("dsa")) {
        keyGen = KeyPairGenerator.getInstance("DSA");
      }
      keyGen.initialize(1024, new SecureRandom());
      KeyPair keypair = keyGen.generateKeyPair();
      PrivateKey privkey = keypair.getPrivate();
      PublicKey pubkey = keypair.getPublic();

      /* Set the attributes of the X509 Certificate that will contain the public key.
       * This is a self signed certificate so the issuer and subject will be the same.
       */
      Hashtable<DERObjectIdentifier, String> attrs = new Hashtable<DERObjectIdentifier, String>();
      Vector<DERObjectIdentifier> ordering = new Vector<DERObjectIdentifier>();
      ordering.add(X509Name.CN);

      attrs.put(X509Name.CN, cn);
      X509Name issuerDN = new X509Name(ordering, attrs);
      X509Name subjectDN = new X509Name(ordering, attrs);

      // Certificate valid from now
      Date validFrom = new Date();
      validFrom.setTime(validFrom.getTime() - (10 * 60 * 1000));
      Date validTo = new Date();
      validTo.setTime(validTo.getTime() + (20 * (24 * 60 * 60 * 1000)));

      // Initialise the X509 Certificate information...
      X509V3CertificateGenerator x509 = new X509V3CertificateGenerator();
      if (keyType.toLowerCase().equals("rsa")) {
        x509.setSignatureAlgorithm("SHA1withRSA");
      }
      else if (keyType.toLowerCase().equals("dsa")) {
        x509.setSignatureAlgorithm("SHA1withDSA");
      }
      x509.setIssuerDN(issuerDN);
      x509.setSubjectDN(subjectDN);
      x509.setPublicKey(pubkey);
      x509.setNotBefore(validFrom);
      x509.setNotAfter(validTo);
      x509.setSerialNumber(new BigInteger(128, new Random()));

      // ...generate it...
      X509Certificate[] cert = new X509Certificate[1];
      cert[0] = x509.generate(privkey, "BC");

      // ...and add the self signed certificate as the certificate chain
      java.security.cert.Certificate[] chain = new java.security.cert.Certificate[1];
      chain[0] = cert[0];

      // Under the alias, store the X509 Certificate and it's public key...
      ks.setKeyEntry(privateKeyAlias, privkey, privateKeyPassword.toCharArray(), cert);
      // ...and the chain...
      ks.setKeyEntry(privateKeyAlias, privkey, privateKeyPassword.toCharArray(), chain);
      // ...and write the keystore to disk
      FileOutputStream fos = new FileOutputStream(keystoreFile);
      ks.store(fos, keystorePassword.toCharArray());
      fos.close();
    }
    catch(Exception se) {
      /* We'll end up here if a security manager is installed and it refuses us
       * permission to add the BouncyCastle provider
       */
      throw new GuanxiException(se);
    }
  }

  /**
   * Creates an empty truststore
   *
   * @param trustStoreFile Full path and name of the truststore to create
   * @param trustStorePassword Password for the truststore
   * @throws GuanxiException if an error occurred
   */
  public void createTrustStore(String trustStoreFile, String trustStorePassword) throws GuanxiException {
    try {
      KeyStore trustStore = KeyStore.getInstance("JKS");
      File truststoreFile = new File(trustStoreFile);
      trustStore.load(null, null);
      FileOutputStream fos = new FileOutputStream(truststoreFile);
      trustStore.store(fos, trustStorePassword.toCharArray());
      fos.close();
    }
    catch(Exception se) {
      throw new GuanxiException(se);
    }
  }
}
