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
   Revision 1.2  2006/07/26 09:38:36  alistairskye
   Added license

   Revision 1.1  2006/07/26 09:37:19  alistairskye
   X509TrustManager to allow probing for server certs via HTTPS

*/

package org.guanxi.common.security.ssl;

import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;
import java.security.cert.CertificateException;

/**
 * Instance of this interface manage which X509 certificates may be used to authenticate the remote side
 * of a secure socket. Decisions may be based on trusted certificate authorities, certificate revocation
 * lists, online status checking or other means
 *
 * @author Alistair Young alistair@smo.uhi.ac.uk
 */
public class GuanxiX509ProbingTrustManager implements X509TrustManager {
  /**
   * Given the partial or complete certificate chain provided by the peer, build a certificate path to a
   * trusted root and return if it can be validated and is trusted for client SSL authentication based on
   * the authentication type. The authentication type is determined by the actual certificate used.
   * For instance, if RSAPublicKey is used, the authType should be "RSA". Checking is case-sensitive
   *
   * @param chain The peer certificate chain
   * @param authType The authentication type based on the client certificate
   * @throws IllegalArgumentException - if null or zero-length chain is passed in for the chain parameter
   * or if null or zero-length string is passed in for the authType parameter
   * @throws CertificateException - if the certificate chain is not trusted by this TrustManager
   */
  public void checkClientTrusted(X509Certificate[] chain, String authType) throws IllegalArgumentException, CertificateException {
  }

  /**
   * Given the partial or complete certificate chain provided by the peer, build a certificate path to a
   * trusted root and return if it can be validated and is trusted for server SSL authentication based on
   * the authentication type. The authentication type is the key exchange algorithm portion of the cipher
   * suites represented as a String, such as "RSA", "DHE_DSS". Note: for some exportable cipher suites,
   * the key exchange algorithm is determined at run time during the handshake. For instance,
   * for TLS_RSA_EXPORT_WITH_RC4_40_MD5, the authType should be RSA_EXPORT when an ephemeral RSA key is
   * used for the key exchange, and RSA when the key from the server certificate is used. Checking is
   * case-sensitive
   *
   * @param chain The peer certificate chain
   * @param authType The key exchange algorithm used
   * @throws IllegalArgumentException - if null or zero-length chain is passed in for the chain parameter
   * or if null or zero-length string is passed in for the authType parameter
   * @throws CertificateException - if the certificate chain is not trusted by this TrustManager
   */
  public void checkServerTrusted(X509Certificate[] chain, String authType) throws IllegalArgumentException, CertificateException {
  }

  /**
   * Return an array of certificate authority certificates which are trusted for authenticating peers
   *
   * @return A non-null (possibly empty) array of acceptable CA issuer certificates
   */
  public X509Certificate[] getAcceptedIssuers() {
    return new X509Certificate[]{};
  }
}
