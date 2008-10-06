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

package org.guanxi.common.job;

import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

/**
 * Implementation of core configuration information passed to jobs. All jobs get this information.
 * Those that require specific settings should override this class
 */
public abstract class SimpleGuanxiJobConfig implements GuanxiJobConfig, ServletContextAware {
  /** The servlet context */
  protected ServletContext servletContext = null;
  /** The key under which the job will be stored in the scheduler */
  protected String key = null;
  /** The running schedule for the job */
  protected String cronLine = null;
  /** The job class to use. A new one is created by Quartz on every invocation */
  public String jobClass = null;
  /** Custom data for a job */
  protected Object privateData = null;

  public void init() {
  }

  /**
   * Works out whether to use a relative or absolute path for a file.
   * If path starts with /WEB-INF or WEB-INF, then it uses the path
   * relative to the web application root, otherwise it just returns
   * the path as-is.
   *
   * @param path The relative or absolute path
   * @return Either the path relative to the webapp root or as-is
   */
  protected String sanitisePath(String path) {
    if ((path.startsWith("WEB-INF")) ||
        (path.startsWith("/WEB-INF"))) {
      return servletContext.getRealPath(path);
    }
    else {
      return path;
    }
  }

  public void setKey(String key) { this.key = key; }
  public void setCronLine(String cronLine) { this.cronLine = cronLine; }
  public String getKey() { return key; }
  public String getCronLine() { return cronLine; }
  public void setJobClass(String jobClass) { this.jobClass = jobClass; }
  public String getJobClass() { return jobClass; }

  public void setPrivateData(Object privateData) { this.privateData = privateData; }
  public Object getPrivateData() { return privateData; }

  public void setServletContext(ServletContext servletContext) { this.servletContext = servletContext; }
  public ServletContext getServletContext() { return servletContext; }
}
