/* CVS Header
   $Id$
   $Log$
   Revision 1.2  2005/07/21 09:15:12  alistairskye
   Added javadoc

   Revision 1.1  2005/07/21 09:14:02  alistairskye
   System wide Logging definitions

*/

package org.guanxi.common.definitions;

/**
 * <font size=5><b></b></font>
 *
 * @author Alistair Young alistair@smo.uhi.ac.uk
 */
public class Logging {
  /** The location where plugins and system classes can put their log files */
  public static final String DEFAULT_LOG_DIR = "/WEB-INF/logs/";

  /** The system's logging config file */
  public static final String DEFAULT_CONFIG_FILE = "/WEB-INF/config/log4j.xml";

  /** Default date format for loggers to use */
  public static final String DEFAULT_LAYOUT = "%d{dd MMMM yyyy HH:mm:ss} - %m%n";
}
