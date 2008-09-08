/* CVS Header
   $
   $
*/

package org.guanxi.common.job;

import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;

import org.apache.log4j.Hierarchy;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.web.context.ServletContextAware;

/**
 * Implementation of core configuration information passed to jobs. All jobs get this information.
 * Those that require specific settings should override this class
 */
public abstract class SimpleGuanxiJobConfig implements GuanxiJobConfig, ServletContextAware {
  /**
   * This is a map of all of the separate logger repositories that exist.
   * These can be used to configure job based loggers separately from the
   * main application loggers.
   */
  private static final Map<String, Hierarchy> loggerRepositories = new TreeMap<String, Hierarchy>();
  
  /** 
   * The servlet context 
   */
  private ServletContext servletContext = null;
  /** 
   * The key under which the job will be stored in the scheduler 
   */
  private String key = null;
  /** 
   * The running schedule for the job 
   */
  private String cronLine = null;
  /** 
   * The job class to use. A new one is created by Quartz on every invocation 
   */
  public String jobClass = null;
  /** 
   * Custom data for a job 
   */
  private Object privateData = null;
  /**
   * This is the configuration file to use for the logging for this job.
   */
  private String loggerConfigurationFile = null;
  
  /**
   * This will create a logger that is configured using the file provided.
   * This makes the following assumptions based on the file name:
   * 1) A file name ending in .xml is an XML file (case insensitive)
   * 2) Anything else is a properties file
   * 
   * @param configurationFile The file to use to configure the logger. This should be the complete path to the file.
   * @param loggerName        This is the name of the logger to return.
   * @param repositoryName    This is the name of the repository to use. If this is null then no special handling is performed. 
   *                          Otherwise a Logger Hierarchy is created or retrieved for the given name and used to hold the logging 
   *                          configuration.
   * @return
   */
  public static Logger createLogger(String configurationFile, String loggerName, String repositoryName) {
    Hierarchy hierarchy;
    Logger    logger;
    
    if ( repositoryName == null ) {
      if ( configurationFile.toLowerCase().endsWith(".xml") ) {
        DOMConfigurator.configure(configurationFile);
      }
      else {
        PropertyConfigurator.configure(configurationFile);
      }
      
      return Logger.getLogger(loggerName);
    }
    else if ( ! loggerRepositories.containsKey(repositoryName) ) {
      logger    = new Logger(null){};
      hierarchy = new Hierarchy(logger);
      
      loggerRepositories.put(repositoryName, hierarchy);
    }
    
    hierarchy = loggerRepositories.get(repositoryName);
    if ( configurationFile.toLowerCase().endsWith(".xml") ) {
      new DOMConfigurator().doConfigure(configurationFile, hierarchy);
    }
    else {
      new PropertyConfigurator().doConfigure(configurationFile, hierarchy);
    }
    
    return hierarchy.getLogger(loggerName);
  }

  /**
   * @return the servletContext
   */
  public ServletContext getServletContext() {
    return servletContext;
  }

  /**
   * @param servletContext the servletContext to set
   */
  public void setServletContext(ServletContext servletContext) {
    this.servletContext = servletContext;
  }

  /**
   * @return the key
   */
  public String getKey() {
    return key;
  }

  /**
   * @param key the key to set
   */
  public void setKey(String key) {
    this.key = key;
  }

  /**
   * @return the cronLine
   */
  public String getCronLine() {
    return cronLine;
  }

  /**
   * @param cronLine the cronLine to set
   */
  public void setCronLine(String cronLine) {
    this.cronLine = cronLine;
  }

  /**
   * @return the jobClass
   */
  public String getJobClass() {
    return jobClass;
  }

  /**
   * @param jobClass the jobClass to set
   */
  public void setJobClass(String jobClass) {
    this.jobClass = jobClass;
  }

  /**
   * @return the privateData
   */
  public Object getPrivateData() {
    return privateData;
  }

  /**
   * @param privateData the privateData to set
   */
  public void setPrivateData(Object privateData) {
    this.privateData = privateData;
  }

  /**
   * @return the loggerConfigurationFile
   */
  public String getLoggerConfigurationFile() {
    return loggerConfigurationFile;
  }

  /**
   * @param loggerConfigurationFile the loggerConfigurationFile to set
   */
  public void setLoggerConfigurationFile(String loggerConfigurationFile) {
    this.loggerConfigurationFile = loggerConfigurationFile;
  }
}
