/* CVS Header
   $
   $
*/

package org.guanxi.common.job;

import org.apache.log4j.Logger;
import org.guanxi.common.log.Log4JLoggerConfig;
import org.guanxi.common.log.Log4JLogger;
import org.guanxi.common.GuanxiException;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

/**
 * Implementation of core configuration information passed to jobs. All jobs get this information.
 * Those that require specific settings should override this class
 */
public abstract class SimpleGuanxiJobConfig implements GuanxiJobConfig, ServletContextAware {
  /** The servlet context */
  protected ServletContext servletContext = null;
  /** Our logger */
  protected Logger log = null;
  /** The logger config */
  protected Log4JLoggerConfig loggerConfig = null;
  /** The Logging setup to use */
  protected Log4JLogger logger = null;
  /** The key under which the job will be stored in the scheduler */
  protected String key = null;
  /** The running schedule for the job */
  protected String cronLine = null;
  /** The job class to use. A new one is created by Quartz on every invocation */
  public String jobClass = null;
  /** Custom data for a job */
  protected Object privateData = null;

  public void init() {
    try {
      loggerConfig.setClazz(Class.forName(jobClass));

      // Sort out the file paths for logging
      loggerConfig.setLogConfigFile(servletContext.getRealPath(loggerConfig.getLogConfigFile()));
      loggerConfig.setLogFile(servletContext.getRealPath(loggerConfig.getLogFile()));

      // Get our logger
      log = logger.initLogger(loggerConfig);
    }
    catch(ClassNotFoundException cnfe) {
      System.err.println("Error loading Job class : " + cnfe);
    }
    catch(GuanxiException ge) {
      System.err.println("Error initialising Job logging : " + ge);
    }
  }

  public void setKey(String key) { this.key = key; }
  public void setCronLine(String cronLine) { this.cronLine = cronLine; }
  public String getKey() { return key; }
  public String getCronLine() { return cronLine; }
  public void setJobClass(String jobClass) { this.jobClass = jobClass; }
  public String getJobClass() { return jobClass; }

  public void setLog(Logger log) { this.log = log; }
  public Logger getLog() { return log; }

  public void setLoggerConfig(Log4JLoggerConfig loggerConfig) { this.loggerConfig = loggerConfig; }
  public Log4JLoggerConfig getLoggerConfig() { return loggerConfig; }

  public void setLogger(Log4JLogger logger) { this.logger = logger; }
  public Log4JLogger getLogger() { return logger; }

  public void setPrivateData(Object privateData) { this.privateData = privateData; }
  public Object getPrivateData() { return privateData; }

  public void setServletContext(ServletContext servletContext) { this.servletContext = servletContext; }
  public ServletContext getServletContext() { return servletContext; }
}
