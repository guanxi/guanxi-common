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

import org.guanxi.common.metadata.Metadata;
import org.guanxi.common.GuanxiException;

import java.security.cert.X509Certificate;

/**
 * Encapsulation of basic trust
 *
 * @author alistair
 */
public interface TrustEngine {
  /**
   * Add a Certificate Authority (CA) X509 certificate to the trust engine.
   * Each CA X509 represents a trust anchor when verifying the claims
   * of an entity.
   *
   * @param x509CACert X509Certificate of a trusted CA
   */
  public void addCACert(X509Certificate x509CACert);

  /**
   * Retrieves all the CA certs the trust engine is using as trust anchors
   *
   * @return Array of X509Certificate objects representing the CA trust anchors
   */
  public X509Certificate[] getCACerts();

  /**
   * Removes all trust information from the engine
   */
  public void reset();
  
  /**
   * Apply the rules of the engine to determine if an entity is to be
   * trusted. If an entity is trusted according to the rules implemented
   * by the engine, then the claims the entity makes can be trusted.
   *
   * @param entityMetadata the Metadata for the entity
   * @param entityData entity specific data, such as a SAML AuthenticationStatement
   * @throws GuanxiException if an error occurs
   * @return true if the entity is trusted, otherwise false
   */
  public boolean trustEntity(Metadata entityMetadata, Object entityData) throws GuanxiException;
}
