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

  /** Default location where implementations that share the same files can find their config files */
  public static final String DEFAULT_SHARED_CONFIG_DIR = "/WEB-INF/guanxi_idp/config/shared/";

  /** Default location where the AuthCookieHandler implementations can find their config files */
  public static final String DEFAULT_AUTHCOOKIEHANDLER_CONFIG_DIR = "/WEB-INF/guanxi_idp/config/cookies/";

  /** Default location where the IdP filter implementations can find their config files */
  public static final String DEFAULT_FILTERS_CONFIG_DIR = "/WEB-INF/guanxi_idp/config/filters/";

  /** Default Attribute Release Policy (ARP) file. Attributors should put their policies in here */
  public static final String DEFAULT_ARP_FILE = DEFAULT_SHARED_CONFIG_DIR + "arp.xml";

  /** Default attribute mapping file. Attributors should put their mapping rules in here */
  public static final String DEFAULT_MAP_FILE = DEFAULT_SHARED_CONFIG_DIR + "map.xml";

  /** The Guanxi IdP XML namespace */
  public static final String NS_IDP_NAME_IDENTIFIER = "urn:guanxi:idp";

  /** The Guanxi SP XML namespace */
  public static final String NS_SP_NAME_IDENTIFIER = "urn:guanxi:sp";

  /** The web.xml parameter name for specifying the log directory for the components */
  public static final String LOGDIR_PARAMETER = "log-dir";

  /** The web.xml parameter name for specifying the log file for each component */
  public static final String LOGFILE_PARAMETER = "log-file";

  /** The name of the BouncyCastle provider */
  public static final String BOUNCY_CASTLE_PROVIDER_NAME = "BC";

  /** Engine context attribute indicating the Engine has finished initialising */
  public static final String CONTEXT_ATTR_ENGINE_INIT_DONE = "INIT";

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

  /** The servlet context attribute that holds the Engine's config object */
  public static final String CONTEXT_ATTR_ENGINE_CONFIG = "CONTEXT_ATTR_ENGINE_CONFIG";
  /** The servlet context attribute that holds the Guard's config object */
  public static final String CONTEXT_ATTR_GUARD_CONFIG = "CONTEXT_ATTR_GUARD_CONFIG";
  /** The servlet context attribute that holds the IdP's config object */
  public static final String CONTEXT_ATTR_IDP_CONFIG = "CONTEXT_ATTR_IDP_CONFIG";
  /** The servlet context attribute that holds the IdP's config document object */
  public static final String CONTEXT_ATTR_IDP_CONFIG_DOC = "CONTEXT_ATTR_IDP_CONFIG_DOC";
  /** The servlet context attribute that holds the filters config object */
  public static final String CONTEXT_ATTR_FILTERS_CONFIG = "CONTEXT_ATTR_FILTERS_CONFIG";

  /** The servlet context attribute that holds the shibmeta metadata extensions */
  public static final String CONTEXT_ATTR_ENGINE_SHIBMETA = "CONTEXT_ATTR_ENGINE_SHIBMETA";

  public static final String CONTEXT_ATTR_X509_CHAIN = "CONTEXT_ATTR_X509_CHAIN";


  /** The servlet context attribute that holds the Guard's cookie prefix */
  public static final String CONTEXT_ATTR_GUARD_COOKIE_PREFIX = "CONTEXT_ATTR_GUARD_COOKIE_PREFIX";
  /** The servlet context attribute that holds the ID of that webapp's Guard */
  public static final String CONTEXT_ATTR_GUARD_ID = "CONTEXT_ATTR_GUARD_ID";
  /** The servlet context attribute that holds the full cookie name that webapp's Guard */
  public static final String CONTEXT_ATTR_GUARD_COOKIE_NAME = "CONTEXT_ATTR_GUARD_COOKIE_NAME";

  /** The servlet context attribute that holds the IdP's cookie prefix */
  public static final String CONTEXT_ATTR_IDP_COOKIE_PREFIX = "CONTEXT_ATTR_IDP_COOKIE_PREFIX";
  /** The servlet context attribute that holds the ID of IdP */
  public static final String CONTEXT_ATTR_IDP_ID = "CONTEXT_ATTR_IDP_ID";
  /** The servlet context attribute that holds the full cookie name of the IdP */
  public static final String CONTEXT_ATTR_IDP_COOKIE_NAME = "CONTEXT_ATTR_IDP_COOKIE_NAME";
  /** The servlet context attribute that holds the cookie domain of the IdP */
  public static final String CONTEXT_ATTR_IDP_COOKIE_DOMAIN = "CONTEXT_ATTR_IDP_COOKIE_DOMAIN";
  /** The servlet context attribute that holds the cookie age of the IdP */
  public static final String CONTEXT_ATTR_IDP_COOKIE_AGE = "CONTEXT_ATTR_IDP_COOKIE_AGE";
  /** The request attribute that holds the Guanxi principal for the IdP */
  public static final String REQUEST_ATTR_IDP_PRINCIPAL = "REQUEST_ATTR_IDP_PRINCIPAL";
  /** The request attribute that determines whether the application in which the IdP is embedded has
    *  authenticated the user via a cookie */
  public static final String REQUEST_ATTR_IDP_COOKIE_AUTHENTICATED = "REQUEST_ATTR_IDP_COOKIE_AUTHENTICATED";
  /** The name of the default authenticator page for the IdP */
  public static final String DEFAULT_AUTH_PAGE_MARKER = "__DEFAULT__";
}
