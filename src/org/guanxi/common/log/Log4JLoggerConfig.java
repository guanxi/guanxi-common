/* CVS Header
   $Id$
   $Log$
   Revision 1.1  2007/10/26 08:07:41  alistairskye
   Config for log4j logging class for use with Spring

*/

package org.guanxi.common.log;

/**
 * Bean for encapsulating logging configuration information
 */
public class Log4JLoggerConfig {
  /** The class that will use the logger. This is the only property not injected. It's set at runtime */
  private Class clazz = null;
  /** Full path/name of the log file to use */
  private String logFile = null;
  /** Full path/name of the logging system's config file */
  private String logConfigFile = null;
  /** Logging layout to use */
  private String logLayout = null;
  /** The maximum size of the log file before rotating it */
  private String logMaxFileSize = null;
  /** The maximum number of rotated log files to keep */
  private String logMaxBackupIndex = null;

  // Setters
  public void setClazz(Class clazz) { this.clazz = clazz; }
  public void setLogFile(String logFile) { this.logFile = logFile; }
  public void setLogConfigFile(String logConfigFile) { this.logConfigFile = logConfigFile; }
  public void setLogLayout(String logLayout) { this.logLayout = logLayout; }
  public void setLogMaxFileSize(String logMaxFileSize) { this.logMaxFileSize = logMaxFileSize; }
  public void setLogMaxBackupIndex(String logMaxBackupIndex) { this.logMaxBackupIndex = logMaxBackupIndex; }

  // Getters
  public Class getClazz() { return clazz; }
  public String getLogFile() { return logFile; }
  public String getLogConfigFile() { return logConfigFile; }
  public String getLogLayout() { return logLayout; }
  public String getLogMaxFileSize() { return logMaxFileSize; }
  public String getLogMaxBackupIndex() { return logMaxBackupIndex; }  
}
