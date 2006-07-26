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
   Revision 1.4  2006/07/26 09:16:20  alistairskye
   Added getServerCertificates(), disconnect() and javadocs.
   Added new boolean parameter to the constructor to allow for probing servers for their certificates. This tells EntityConnection to use a special Guanxi TrustManager that allows HTTPS connections in order to inspect the certificate

   Revision 1.3  2006/05/16 09:04:58  alistairskye
   Added setRequestProperty(String, String)

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
import javax.net.ssl.SSLPeerUnverifiedException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.cert.Certificate;

/**
 * Wraps either an HttpsURLConnection or HttpsURLConnection. The HttpsURLConnection
 * uses the org.guanxi.common.security.ssl.SSL defined custom SSL layer.
 *
 * @author Alistair Young alistair@smo.uhi.ac.uk
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
                          String trustStore, String trustStorePassword, boolean probeForServerCert) throws GuanxiException {
    try {
      SSLContext context = SSLContext.getInstance("SSL");
      context.init(SSL.getKeyManagers(localEntityID, entityKeystore, entityKeystorePassword),
                   SSL.getTrustManagers(trustStore, trustStorePassword, probeForServerCert), null);

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

  /**
   * Set the method for the URL request, one of:
   * GET
   * POST
   * HEAD
   * OPTIONS
   * PUT
   * DELETE
   * TRACE
   * are legal, subject to protocol restrictions. The default method is GET
   *
   * @param requestMethod The HTTP method
   * @throws GuanxiException
   */
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

  /**
   * Sets the value of the doOutput field for this EntityConnection to the specified value.
   * A URL connection can be used for input and/or output. Set the DoOutput flag to true
   * if you intend to use the EntityConnection for output, false if not. The default is false.
   *
   * @param doIt The new value
   * @throws GuanxiException
   */
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

  /**
   * Opens a communications link to the resource referenced by this URL, if such a connection
   * has not already been established.
   * If the connect method is called when the connection has already been opened (indicated by
   * the connected field having the value true), the call is ignored.
   * EntityConnection objects go through two phases: first they are created, then they are connected.
   * After being created, and before being connected, various options can be specified
   * (e.g., doInput and UseCaches). After connecting, it is an error to try to set them.
   * Operations that depend on being connected, like getContentLength, will implicitly perform the connection,
   * if necessary
   *
   * @throws GuanxiException
   */
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

  /**
   * Returns an input stream that reads from this open connection. A SocketTimeoutException can be
   * thrown when reading from the returned input stream if the read timeout expires before data is
   * available for read
   *
   * @return An input stream that reads from this open connection
   * @throws GuanxiException
   */
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

  /**
   * Returns an output stream that writes to this connection
   *
   * @return An output stream that writes to this connection
   * @throws GuanxiException
   */
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

  /**
   * Sets the general request property. If a property with the key already exists, overwrite its
   * value with the new value.
   * NOTE: HTTP requires all request properties which can legally have multiple instances with the
   * same key to use a comma-seperated list syntax which enables multiple properties to be appended
   * into a single property
   *
   * @param key The keyword by which the request is known (e.g., "accept").
   * @param value The value associated with it
   */
  public void setRequestProperty(String key, String value) {
    if (secure)
      httpsURL.setRequestProperty(key, value);
    else
      httpURL.setRequestProperty(key, value);
  }

  /**
   * Returns the server's certificate chain which was established as part of defining the session.
   * Note: This method can be used only when using certificate-based cipher suites; using it with
   * non-certificate-based cipher suites, such as Kerberos, will cause it to return null.
   *
   * @return An ordered array of server certificates, with the peer's own certificate first followed
   * by any certificate authorities. If an error occurred it will return null.
   */
  public Certificate[] getServerCertificates() {
    if (secure) {
      try {
        return httpsURL.getServerCertificates();
      }
      catch(SSLPeerUnverifiedException spue) {
        return null;
      }
    }
    else
      return null;
  }

  /**
   * Indicates that other requests to the server are unlikely in the near future.
   * Calling disconnect() should not imply that this HttpURLConnection instance
   * can be reused for other requests
   */
  public void disconnect() {
    if (secure)
      httpsURL.disconnect();
    else
      httpURL.disconnect();
  }
}
