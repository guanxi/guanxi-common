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
   Revision 1.16  2006/11/22 14:46:52  alistairskye
   Added new constants to support REST services
   Added new constants for holding keystores and truststores to support REST services

   Revision 1.15  2006/11/20 11:54:59  alistairskye
   Oops, BOUNCY_CASTLE_PROVIDER_NAME was private!

   Revision 1.14  2006/11/20 11:54:02  alistairskye
   Added BOUNCY_CASTLE_PROVIDER_NAME

   Revision 1.13  2006/07/26 13:23:08  alistairskye
   Added:
   AXIS_PROPERTY_CERT_PROBING
   AXIS_PROPERTY_CERT_PROBING_ON
   AXIS_PROPERTY_CERT_PROBING_OFF

   Revision 1.12  2006/07/25 12:52:24  alistairskye
   Added GUARD_CONFIG_OBJECT

   Revision 1.11  2006/07/25 11:34:17  alistairskye
   Added ENGINE_CONFIG_OBJECT

   Revision 1.10  2006/07/25 11:08:28  alistairskye
   Added AxisProperties definitions for secure web services communication

   Revision 1.9  2006/01/21 15:35:54  alistairskye
   Updated to use new WEB-INF/guanxi_idp/config location

   Revision 1.8  2005/11/03 16:06:50  alistairskye
   Added LOGDIR_PARAMETER

   Revision 1.7  2005/11/03 15:53:51  alistairskye
   Added LOGFILE_PARAMETER

   Revision 1.6  2005/07/21 11:01:50  alistairskye
   Fixed bug with DEFAULT_AUTHCOOKIEHANDLER_CONFIG_DIR

   Revision 1.5  2005/07/21 09:10:34  alistairskye
   Added DEFAULT_AUTHENTICATOR_CONFIG_DIR and DEFAULT_AUTHCOOKIEHANDLER_CONFIG_DIR

   Revision 1.4  2005/07/21 09:06:42  alistairskye
   Added DEFAULT_ATTRIBUTOR_CONFIG_DIR
   Added javadocs

   Revision 1.3  2005/07/19 14:15:18  alistairskye
   Added NS_IDP_NAME_IDENTIFIER

   Revision 1.2  2005/07/11 10:51:34  alistairskye
   Package restructure

   Revision 1.1  2005/06/28 11:33:45  alistairskye
   Guanxi definitions

*/

package org.guanxi.common.definitions;

/**
 * <font size=5><b></b></font>
 *
 * @author Alistair Young alistair@smo.uhi.ac.uk
 */
public class Guanxi {
  /** Default location where the Authenticator implementations can find their config files */
  public static final String DEFAULT_AUTHENTICATOR_CONFIG_DIR = "/WEB-INF/guanxi_idp/config/authenticators/";

  /** Default location where the Attributor implementations can find their config files */
  public static final String DEFAULT_ATTRIBUTOR_CONFIG_DIR = "/WEB-INF/guanxi_idp/config/attributors/";

  /** Default location where the AuthCookieHandler implementations can find their config files */
  public static final String DEFAULT_AUTHCOOKIEHANDLER_CONFIG_DIR = "/WEB-INF/guanxi_idp/config/cookies/";

  /** The Guanxi IdP XML namespace */
  public static final String NS_IDP_NAME_IDENTIFIER = "urn:guanxi:idp";

  /** The Guanxi SP XML namespace */
  public static final String NS_SP_NAME_IDENTIFIER = "urn:guanxi:sp";

  /** The web.xml parameter name for specifying the log directory for the components */
  public static final String LOGDIR_PARAMETER = "log-dir";

  /** The web.xml parameter name for specifying the log file for each component */
  public static final String LOGFILE_PARAMETER = "log-file";

  /** The key under which the Engine's configuration object is stored in the servlet context */
  public static final String ENGINE_CONFIG_OBJECT = "engineConfig";

  /** The key under which the Guard's configuration object is stored in the servlet context */
  public static final String GUARD_CONFIG_OBJECT = "guardConfig";

  // AxisProperty settings for web services

  /** The alias to use within a keystore when making a secure connection */
  public static final String AXIS_PROPERTY_KEYSTORE_ALIAS = "guanxi.alias";
  /** The keystore to use when making a secure connection */
  public static final String AXIS_PROPERTY_KEYSTORE = "guanxi.keystore";
  /** The keystore password to use when making a secure connection */
  public static final String AXIS_PROPERTY_KEYSTORE_PASSWORD = "guanxi.keystorePassword";
  /** The truststore to use when accepting a secure connection */
  public static final String AXIS_PROPERTY_TRUSTSTORE = "guanxi.truststore";
  /** The truststore password to use when accepting a secure connection */
  public static final String AXIS_PROPERTY_TRUSTSTORE_PASSWORD = "guanxi.truststorePassword";
  /** The value of this determines whether the web service call is authenticated */
  public static final String AXIS_PROPERTY_CERT_PROBING = "guanxi.CertProbing";
  /** The value to turn on cert probing in the Guanxi SSL layer */
  public static final String AXIS_PROPERTY_CERT_PROBING_ON = "on";
  /** The value to turn off cert probing in the Guanxi SSL layer */
  public static final String AXIS_PROPERTY_CERT_PROBING_OFF = "off";

  /** The name of the BouncyCastle provider */
  public static final String BOUNCY_CASTLE_PROVIDER_NAME = "BC";

  /** Engine context attribute indicating the Engine has finished initialising */
  public static final String CONTEXT_ATTR_ENGINE_INIT_DONE = "INIT";

  /** Absolute path to Guards metadata dir stored under this Engine servlet context key */
  public static final String CONTEXT_ATTR_GUARDS_METADATA_DIR = "GUARDSMETADATADIR";
  /** Absolute path to IdP metadata dir stored under this Engine servlet context key */
  public static final String CONTEXT_ATTR_IDP_METADATA_DIR = "IDPMETADATADIR";

  /** Absolute path to Engine's keystore stored under this Engine servlet context key */
  public static final String CONTEXT_ATTR_ENGINE_KEYSTORE = "ENGINEKEYSTORE";
  /** Engine's keystore password stored under this Engine servlet context key */
  public static final String CONTEXT_ATTR_ENGINE_KEYSTORE_PASSWORD = "ENGINEKEYSTOREPASSWORD";
  /** Absolute path to Engine's truststore stored under this Engine servlet context key */
  public static final String CONTEXT_ATTR_ENGINE_TRUSTSTORE = "ENGINETRUSTSTORE";
  /** Engine's truststore password stored under this Engine servlet context key */
  public static final String CONTEXT_ATTR_ENGINE_TRUSTSTORE_PASSWORD = "ENGINETRUSTSTOREPASSWORD";

  /** Absolute path to Guard's keystore stored under this Guard servlet context key */
  public static final String CONTEXT_ATTR_GUARD_KEYSTORE = "GUARDKEYSTORE";
  /** Guard's keystore password stored under this Guard servlet context key */
  public static final String CONTEXT_ATTR_GUARD_KEYSTORE_PASSWORD = "GUARDKEYSTOREPASSWORD";
  /** Absolute path to Guard's truststore stored under this Guard servlet context key */
  public static final String CONTEXT_ATTR_GUARD_TRUSTSTORE = "GUARDTRUSTSTORE";
  /** Guard's truststore password stored under this Guard servlet context key */
  public static final String CONTEXT_ATTR_GUARD_TRUSTSTORE_PASSWORD = "GUARDTRUSTSTOREPASSWORD";

  /** The Guard ID request parameter for WAYFLocation service */
  public static final String WAYF_PARAM_GUARD_ID = "guardid";
  /** The Guard Session ID request parameter for WAYFLocation service */
  public static final String WAYF_PARAM_SESSION_ID = "sessionid";

  /** The Guard Session ID request parameter for SessionVerifier service */
  public static final String SESSION_VERIFIER_PARAM_SESSION_ID = "sessionid";
  /** SessionVerifier return value indicating session was verified */
  public static final String SESSION_VERIFIER_RETURN_VERIFIED = "verified";
  /** SessionVerifier return value indicating session was not verified */
  public static final String SESSION_VERIFIER_RETURN_NOT_VERIFIED = "notverified";
}
