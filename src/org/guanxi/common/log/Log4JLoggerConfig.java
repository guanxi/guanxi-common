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
