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

import org.apache.xml.security.signature.XMLSignature;

/**
 * Class for configuring methods in SecUtils
 *
 * @author Alistair Young
 */
public class SecUtilsConfig {
  public static final String KEY_TYPE_DSA = "DSA";
  public static final String KEY_TYPE_RSA = "RSA";

  /** Keystore type. Currently only support JKS */
  String keystoreType = null;
  /** Full path and name of the keystore */
  String keystoreFile = null;
  /** Password for the keystore */
  String keystorePass = null;
  /** Alias in the keystore under which the private key is stored */
  String privateKeyAlias = null;
  /** Password for the private key */
  String privateKeyPass = null;
  /** Alias in the keystore under which the public key certificate is stored */
  String certificateAlias = null;
  /** The type of the private key */
  String keyType = null;

  /**
   * Default constructor
   */
  public SecUtilsConfig() {}

  public void setKeystoreType(String keystoreType) { this.keystoreType = keystoreType; }
  public void setKeystoreFile(String keystoreFile) { this.keystoreFile = keystoreFile; }
  public void setKeystorePass(String keystorePass) { this.keystorePass = keystorePass; }
  public void setPrivateKeyAlias(String privateKeyAlias) { this.privateKeyAlias = privateKeyAlias; }
  public void setPrivateKeyPass(String privateKeyPass) { this.privateKeyPass = privateKeyPass; }
  public void setCertificateAlias(String certificateAlias) { this.certificateAlias = certificateAlias; }
  public void setKeyType(String keyType) {
    if (keyType.equalsIgnoreCase(KEY_TYPE_DSA))
      this.keyType = XMLSignature.ALGO_ID_SIGNATURE_DSA;
    if (keyType.equalsIgnoreCase(KEY_TYPE_RSA))
      this.keyType = XMLSignature.ALGO_ID_SIGNATURE_RSA;
  }

  public String getKeystoreType() { return keystoreType; }
  public String getKeystoreFile() { return keystoreFile; }
  public String getKeystorePass() { return keystorePass; }
  public String getPrivateKeyAlias() { return privateKeyAlias; }
  public String getPrivateKeyPass() { return privateKeyPass; }
  public String getCertificateAlias() { return certificateAlias; }
  public String getKeyType() { return keyType; }
}
