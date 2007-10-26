/* CVS Header
   $Id$
   $Log$
   Revision 1.1  2007/10/26 08:07:20  alistairskye
   log4j logging class for use with Spring

   Revision 1.1  2006/08/17 14:59:36  alistair
   Encapsulates logging initialisation

*/

package org.guanxi.common.log;

import org.apache.log4j.xml.DOMConfigurator;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.Logger;
import org.guanxi.common.GuanxiException;

/**
 * Class that encapsulates logging using log4j
 *
 * @author Alistair Young alistair@googlemail.com
 */
public class Log4JLogger {
  /** The full path/name of the log4j config file */
  private String logConfigFile = null;
  /** Default date format for loggers to use */
  private String logLayout = null;
  /** The maximum size of the log file */
  private String logMaxFileSize = null;
  private int logMaxBackupIndex;

  /**
   * Initialises a Logger object with the setting given to the class at startup.
   *
   * @param config The Log4JLoggerConfig instance to use for configuration
   * @return Logger object initialised to the current settings
   * @throws GuanxiException if an error occurs
   */
  public Logger initLogger(Log4JLoggerConfig config) throws GuanxiException {
    Logger log = Logger.getLogger(config.getClazz());

    PatternLayout defaultLayout = new PatternLayout(logLayout);

    RollingFileAppender rollingFileAppender = new RollingFileAppender();
    rollingFileAppender.setName(config.getClazz().getName());
    try {
      rollingFileAppender.setFile(config.getLogFile(), true, false, 0);
    }
    catch(Exception e) {
      throw new GuanxiException(e);
    }
    rollingFileAppender.setMaxFileSize(logMaxFileSize);
    rollingFileAppender.setMaxBackupIndex(logMaxBackupIndex);
    rollingFileAppender.setLayout(defaultLayout);

    log.removeAllAppenders();
    log.addAppender(rollingFileAppender);
    log.setAdditivity(false);

    return log;
  }

  // Setters
  public void setLogConfigFile(String logConfigFile) {
    this.logConfigFile = logConfigFile;
    DOMConfigurator.configure(this.logConfigFile);
  }
  public void setLogLayout(String logLayout) { this.logLayout = logLayout; }
  public void setLogMaxFileSize(String logMaxFileSize) { this.logMaxFileSize = logMaxFileSize; }
  public void setLogMaxBackupIndex(int logMaxBackupIndex) { this.logMaxBackupIndex = logMaxBackupIndex; }

  // Getters
  public String getLogConfigFile() { return logConfigFile; }
  public String getLogLayout() { return logLayout; }
  public String getLogMaxFileSize() { return logMaxFileSize; }
  public int getLogMaxBackupIndex() { return logMaxBackupIndex; }
}
