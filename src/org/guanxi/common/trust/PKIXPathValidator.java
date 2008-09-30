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

import java.security.cert.X509Certificate;

/**
 * Defines PKIX path validation
 *
 * @author alistair
 */
public interface PKIXPathValidator {
  /**
   * Add Certificate Authority (CA) X509 certificate to the trust engine.
   * Each CA X509 represents a trust anchor when verifying the claims
   * of an entity.
   *
   * @param x509Cert X509Certificate of a trusted CA
   */
  public void addCert(X509Certificate x509Cert);

  /**
   * Removes all trust information from the engine
   */
  public void reset();
}
