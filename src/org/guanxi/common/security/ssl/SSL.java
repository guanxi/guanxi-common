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
   Revision 1.4  2006/07/26 09:27:24  alistairskye
   Updated javadocs

   Revision 1.3  2006/07/26 09:17:51  alistairskye
   Added new boolean parameter to getTrustManagers(). If this is true then the method will return a special Guanxi TrustManager to allow for probing servers for their certificates

   Revision 1.2  2006/05/04 15:25:17  alistairskye
   Updated getKeyManagers() to work on keystore directly instead of building the name from path and guard id.

   Revision 1.1  2006/04/05 14:36:42  alistairskye
   Custom SSL layer for identity masquerading in the SP

*/

package org.guanxi.common.security.ssl;

import javax.net.ssl.*;
import java.security.KeyStore;
import java.io.FileInputStream;

/**
 * Defines trust and keystore managers for HTTPS connections.
 *
 * @author Alistair Young alistair@smo.uhi.ac.uk
 */
public class SSL {
  /**
   * Returns an array of TrustManager objects that the JVM will use to verify or authenticate a remote
   * server when using HTTPS.
   *
   * @param trustStore The full path and name of the truststore to use for server verification.
   * Can be null if probeForServerCert is true.
   * @param password The password for the truststore.
   * Can be null if probeForServerCert is true.
   * @param probeForServerCert If this is true, then an instance of GuanxiX509TrustManager will
   * be returned. This is to allow probing servers to extract their certificates.
   * @return An array of TrustManage objects that can be used by the JVM to verify an HTTPS connection
   *  or null if an error occurred.
   */
  public static TrustManager[] getTrustManagers(String trustStore, String password, boolean probeForServerCert) {
    try {
      // If we're just after the server's certificate, delegate trust checking to Guanxi
      if (probeForServerCert)
        return new TrustManager[]{new GuanxiX509TrustManager()};

      // First, get the default TrustManagerFactory.
      String alg = TrustManagerFactory.getDefaultAlgorithm();
      TrustManagerFactory tmFact = TrustManagerFactory.getInstance(alg);

      // Next, set up the TrustStore to use. We need to load the file into
      // a KeyStore instance.
      FileInputStream fis = new FileInputStream(trustStore);
      KeyStore ks = KeyStore.getInstance("jks");
      ks.load(fis, password.toCharArray());
      fis.close();

      // Now we initialise the TrustManagerFactory with this KeyStore
      tmFact.init(ks);

      // And now get the TrustManagers
      return tmFact.getTrustManagers();
    }
    catch(Exception e) {
      return null;
    }
  }

  /**
   * Returns an array of GuanxiX509KeyManager objects that can be used to added certificates to an HTTPS connection.
   * The Engine uses this method to load a keystore belonging to a Guard, in order to add the Guard's
   * certificate to the HTTPS request to, for example, an AttributeAuthority.
   * Note that only X509KeyManager instances are supported.
   *
   * @param guardID The ID of the Guard who's keystore must be loaded. This is also the alias within the keystore
   * under which the Guard's certificate is stored. It's this certificate that's added to the HTTPS connection.
   * @param keystore The full path and name of the keystore to load.
   * @param password The password for the keystore.
   * @return An array of GuanxiX509KeyManager objects or null if an error occurred.
   */
  public static KeyManager[] getKeyManagers(String guardID, String keystore, String password) {
    try {
      String alg = KeyManagerFactory.getDefaultAlgorithm();
      KeyManagerFactory kmFact = KeyManagerFactory.getInstance(alg);

      FileInputStream fis = new FileInputStream(keystore);
      KeyStore ks = KeyStore.getInstance("jks");
      ks.load(fis, password.toCharArray());
      fis.close();

      kmFact.init(ks, password.toCharArray());

      KeyManager[] keyManagers = kmFact.getKeyManagers();

      for (int c=0; c < keyManagers.length; c++) {
        if (keyManagers[c] instanceof X509KeyManager) {
          keyManagers[c]= new GuanxiX509KeyManager((X509KeyManager)keyManagers[c], guardID);
        }
      }

      return keyManagers;
    }
    catch(Exception e) {
      return null;
    }
  }
}
