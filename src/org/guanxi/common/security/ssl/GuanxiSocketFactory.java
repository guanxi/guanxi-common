/* CVS Header
   $Id$
   $Log$
   Revision 1.3  2006/07/26 09:29:50  alistairskye
   Updated javadocs
   Updated initSSLContext() to pass new probing parameter to the SSL layer

   Revision 1.2  2006/07/25 12:43:55  alistairskye
   Updated create(). Now uses Guanxi definitions to get AxisProperties values

   Revision 1.1  2006/07/25 11:07:25  alistairskye
   Provides custom keystore and truststore handling for secure web services communication

*/

package org.guanxi.common.security.ssl;

import org.apache.axis.components.net.JSSESocketFactory;
import org.apache.axis.components.net.BooleanHolder;
import org.apache.axis.AxisProperties;
import org.guanxi.common.definitions.Guanxi;

import javax.net.ssl.SSLContext;
import java.util.Hashtable;
import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.KeyManagementException;

/**
 * Provides a way to use custom keystores and truststores for Axis calls. The class uses the Guanxi
 * SSL layer to initialise the custom keystore and truststore.
 *
 * @author Alistair Young alistair@smo.uhi.ac.uk
 */
public class GuanxiSocketFactory extends JSSESocketFactory  {
  /**
   * Constructor
   *
   * @param hashtable
   */
  public GuanxiSocketFactory(Hashtable hashtable) {
    // JSSESocketFactory doesn't have a default constructor so we must call it's own constructor here
    super(hashtable);
  }

  /**
   * Initialises the factory. Doesn't seem to be called from Axis.
   *
   * @throws IOException
   */
  protected void initFactory() throws IOException {
  }

  /**
   * Creates a Socket to be used when web services are communicating over HTTPS.
   * The Socket uses a custom Guanxi SSLContext that is configured with it's own keystore and
   * truststore.
   * The following AxisProperties must be set before initiating web services communication via
   * HTTPS:
   *
   * Guanxi.AXIS_PROPERTY_KEYSTORE_ALIAS
   * Guanxi.AXIS_PROPERTY_KEYSTORE
   * Guanxi.AXIS_PROPERTY_KEYSTORE_PASSWORD
   * Guanxi.AXIS_PROPERTY_TRUSTSTORE
   * Guanxi.AXIS_PROPERTY_TRUSTSTORE_PASSWORD
   *
   * @param host The host name to connect to, e.g. guanxi.ac.uk
   * @param port The port to connect to, e.g. 8443. If this is -1 it will default to 443
   * @param otherHeaders
   * @param useFullURL
   * @return
   * @throws Exception
   */
  public Socket create(String host, int port, StringBuffer otherHeaders, BooleanHolder useFullURL)
                       throws Exception {
    if (port == -1)
      port = 443;

    SSLContext context = initSSLContext(AxisProperties.getProperty(Guanxi.AXIS_PROPERTY_KEYSTORE_ALIAS),
                                        AxisProperties.getProperty(Guanxi.AXIS_PROPERTY_KEYSTORE),
                                        AxisProperties.getProperty(Guanxi.AXIS_PROPERTY_KEYSTORE_PASSWORD),
                                        AxisProperties.getProperty(Guanxi.AXIS_PROPERTY_TRUSTSTORE),
                                        AxisProperties.getProperty(Guanxi.AXIS_PROPERTY_TRUSTSTORE_PASSWORD));

    if (context != null) {
      return context.getSocketFactory().createSocket(host, port);
    }
    else
      return null;
  }

  /**
   *
   * @param entityID The alias to use in the keystore
   * @param keystore
   * @param keystorePassword
   * @param truststore
   * @param truststorePassword
   * @return Custom SSLContext configured by the Guanxi SSL layer
   */
  private SSLContext initSSLContext(String entityID, String keystore, String keystorePassword,
                                    String truststore, String truststorePassword) {
    SSLContext context = null;

    try {
      context = SSLContext.getInstance("SSL");
      context.init(SSL.getKeyManagers(entityID, keystore, keystorePassword),
                   SSL.getTrustManagers(truststore, truststorePassword, false), null);
    }
    catch(NoSuchAlgorithmException nsae) {
    }
    catch(KeyManagementException kme) {
    }

    return context;
  }
}
