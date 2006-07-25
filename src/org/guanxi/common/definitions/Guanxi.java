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

import org.apache.axis.AxisProperties;

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
}
