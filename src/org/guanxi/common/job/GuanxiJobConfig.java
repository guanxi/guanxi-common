/* CVS Header
   $
   $
*/

package org.guanxi.common.job;


/**
 * Represents core configuration information passed to jobs. All jobs get this information.
 * Those that require specific settings should override SimpleGuanxiJobConfig
 */
public interface GuanxiJobConfig {
  /** The key in a job's data map where the job's config is stored */
  public static final String JOB_KEY_JOB_CONFIG = "JOB_KEY_JOB_CONFIG";
  
  /** 
   * Sets the key under which the job will be stored in the scheduler 
   */
  public void setKey(String key);
  /** 
   * Gets the key under which the job will be stored in the scheduler 
   */
  public String getKey();
  /** 
   * Gets the running schedule for the job 
   */
  public void setCronLine(String cronLine);
  /** 
   * Sets the running schedule for the job 
   */
  public String getCronLine();
  /** 
   * Gets the job class to use. A new one is created by Quartz on every invocation 
   */
  public void setJobClass(String jobClass);
  /** 
   * Sets the job class to use. A new one is created by Quartz on every invocation 
   */
  public String getJobClass();
  /** 
   * Sets the custom data for a job 
   */
  public void setPrivateData(Object privateData);
  /** 
   * Gets the custom data for a job 
   */
  public Object getPrivateData();
  /**
   * Sets the configuration file to use for the job logger.
   */
  public void setLoggerConfigurationFile(String loggerConfigurationFile);
  /**
   * Gets the configuration file to use for the job logger.
   */
  public String getLoggerConfigurationFile();
  /** 
   * Sets whether to start the job straight away 
   */
  public void setStartImmediately(boolean startImmediately);
  /** 
   * Gets whether to start the job straight away 
   */
  public boolean isStartImmediately();
}
