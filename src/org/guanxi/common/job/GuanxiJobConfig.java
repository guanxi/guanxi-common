/* CVS Header
   $
   $
*/

package org.guanxi.common.job;

import org.apache.log4j.Logger;
import org.guanxi.common.log.Log4JLoggerConfig;
import org.guanxi.common.log.Log4JLogger;

/**
 * Represents core configuration information passed to jobs. All jobs get this information.
 * Those that require specific settings should override SimpleGuanxiJobConfig
 */
public interface GuanxiJobConfig {
  public void setKey(String key);
  public String getKey();
  public void setCronLine(String cronLine);
  public String getCronLine();
  public void setJobClass(String jobClass);
  public String getJobClass();

  public void setLog(Logger log);
  public Logger getLog();

  public void setLoggerConfig(Log4JLoggerConfig loggerConfig);
  public Log4JLoggerConfig getLoggerConfig();

  public void setLogger(Log4JLogger logger);
  public Log4JLogger getLogger();

  public void setPrivateData(Object privateData);
  public Object getPrivateData();
}
