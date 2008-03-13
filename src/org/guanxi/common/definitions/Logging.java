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
public class Logging {
  /** The location where IdP plugins and system classes can put their log files */
  public static final String DEFAULT_IDP_LOG_DIR = "/WEB-INF/guanxi_idp/logs/";

  /** The IdP's logging config file */
  public static final String DEFAULT_IDP_CONFIG_FILE = "/WEB-INF/guanxi_idp/config/idp-log4j.xml";

  /** Where the Engine and Guard can put their log files */
  public static final String DEFAULT_SP_ENGINE_LOG_DIR = "/WEB-INF/logs/";

  /** Where the Engine and Guard can put their log files */
  public static final String DEFAULT_SP_GUARD_LOG_DIR = "/WEB-INF/guanxi_sp_guard/logs/";

  /** The SP Engine's logging config file */
  public static final String DEFAULT_SP_ENGINE_CONFIG_FILE = "/WEB-INF/config/sp-log4j.xml";

  /** The SP Guard's logging config file */
  public static final String DEFAULT_SP_GUARD_CONFIG_FILE = "/WEB-INF/guanxi_sp_guard//config/sp-log4j.xml";

  /** Default date format for loggers to use */
  public static final String DEFAULT_LAYOUT = "%d{dd MMMM yyyy HH:mm:ss} - %m%n";
}
