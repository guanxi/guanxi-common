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

import org.guanxi.common.trust.TrustEngine;

import java.security.cert.X509Certificate;
import java.util.Vector;

/**
 * Abstract TrustEngine for more specialised implementations to use
 *
 * @author alistair
 */
public abstract class SimpleTrustEngine implements TrustEngine {
  /** The CA store used for trust anchors */
  protected Vector<X509Certificate> caCerts = null;

  /**
   * Default constructor
   */
  protected SimpleTrustEngine() {
    // New CA store
    caCerts = new Vector<X509Certificate>();
  }

  /** @see org.guanxi.common.trust.TrustEngine#addCACert(java.security.cert.X509Certificate) */
  public void addCACert(X509Certificate x509CACert) {
    caCerts.add(x509CACert);
  }

  /** @see org.guanxi.common.trust.TrustEngine#getCACerts()  */
  public X509Certificate[] getCACerts() {
    X509Certificate[] x509CACerts = new X509Certificate[caCerts.size()];
    caCerts.copyInto(x509CACerts);
    return x509CACerts;
  }

  /** @see org.guanxi.common.trust.TrustEngine#reset() */
  public void reset() {
    caCerts.clear();
  }
}
