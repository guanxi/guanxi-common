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
   Revision 1.2  2006/05/04 15:24:37  alistairskye
   Added new constructor arguments. Keystore path/name is now passed in instead of path to keystore directory. Trust store and password also now passed in.

   Revision 1.1  2006/04/06 11:06:05  alistairskye
   Class to wrap Http(s)URLConnection objects and use the custom SSL management if required

*/

package org.guanxi.common;

import org.guanxi.common.security.ssl.GuanxiHostVerifier;
import org.guanxi.common.security.ssl.SSL;
import javax.net.ssl.SSLContext;
import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Wraps either an HttpsURLConnection or HttpsURLConnection. The HttpsURLConnection
 * uses the org.guanxi.common.security.ssl.SSL defined custom SSL layer.
 */
public class EntityConnection {
  boolean secure = false;
  HttpsURLConnection httpsURL = null;
  HttpURLConnection httpURL = null;

  /**
   * Sets up a connection object with custom SSL management if required.
   *
   * @param endpoint Address of the entity to connect to. If this starts with https
   * the custom SSL management will be used.
   * @param localEntityID The ID of the local entity which will be represented by the
   * custom SSL layer. If the connection is not secure, this parameter can be null.
   * @param entityKeystore The full path of the entity's keystore
   * @param entityKeystorePassword Password for the entity's keystore
   * @param trustStore The full path to the Engine's truststore
   * @param trustStorePassword The password for the Engine's truststore
   * @throws GuanxiException
   */
  public EntityConnection(String endpoint, String localEntityID, String entityKeystore, String entityKeystorePassword,
                          String trustStore, String trustStorePassword) throws GuanxiException {
    try {
      SSLContext context = SSLContext.getInstance("SSL");
      context.init(SSL.getKeyManagers(localEntityID, entityKeystore, entityKeystorePassword),
                   SSL.getTrustManagers(trustStore, trustStorePassword), null);

      URL url = new URL(endpoint);

      if (endpoint.toLowerCase().startsWith("https")) {
        secure = true;
        httpsURL = (HttpsURLConnection)url.openConnection();
        httpsURL.setSSLSocketFactory(context.getSocketFactory());
        httpsURL.setHostnameVerifier(new GuanxiHostVerifier());
      }
      else {
        httpURL = (HttpURLConnection)url.openConnection();
      }
    }
    catch(Exception e) {
      throw new GuanxiException(e);
    }
  }

  public void setRequestMethod(String requestMethod) throws GuanxiException {
    try {
      if (secure)
        httpsURL.setRequestMethod(requestMethod);
      else
        httpURL.setRequestMethod(requestMethod);
    }
    catch(Exception e) {
      throw new GuanxiException(e);
    }
  }

  public void setDoOutput(boolean doIt) throws GuanxiException {
    try {
      if (secure)
        httpsURL.setDoOutput(doIt);
      else
        httpURL.setDoOutput(doIt);
    }
    catch(Exception e) {
      throw new GuanxiException(e);
    }
  }

  public void connect() throws GuanxiException {
    try {
      if (secure)
        httpsURL.connect();
      else
        httpURL.connect();
    }
    catch(Exception e) {
      throw new GuanxiException(e);
    }
  }

  public InputStream getInputStream() throws GuanxiException {
    try {
      if (secure)
        return httpsURL.getInputStream();
      else
        return httpURL.getInputStream();
    }
    catch(Exception e) {
      throw new GuanxiException(e);
    }
  }

  public OutputStream getOutputStream() throws GuanxiException {
    try {
      if (secure)
        return httpsURL.getOutputStream();
      else
        return httpURL.getOutputStream();
    }
    catch(Exception e) {
      throw new GuanxiException(e);
    }
  }
}
